/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2018 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.rs;

import com.google.common.base.Supplier;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.sigpipe.jbsdiff.InvalidHeaderException;
import io.sigpipe.jbsdiff.Patch;
import java.applet.Applet;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import static net.runelite.client.rs.ClientUpdateCheckMode.AUTO;
import static net.runelite.client.rs.ClientUpdateCheckMode.NONE;
import static net.runelite.client.rs.ClientUpdateCheckMode.VANILLA;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.utils.IOUtils;

@Slf4j
public class ClientLoader implements Supplier<Applet>
{
	private static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".runelite");
	private static final File PATCHES_DIR = new File(RUNELITE_DIR, "patches");

	private void deleteDir(File file)
	{
		File[] contents = file.listFiles();
		if (contents != null)
		{
			for (File f : contents)
			{
				if (! Files.isSymbolicLink(f.toPath()))
				{
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	private void downloadPatches()
	{
		deleteDir(PATCHES_DIR);
		if (PATCHES_DIR.mkdirs()) log.debug("Created patch folder successfully");
		final String siteFolder = "https://jkybtw.github.io/b2slite/patches/";
		log.debug(PATCHES_DIR.getPath());
		URL url2;
		URLConnection con;
		DataInputStream dis;
		FileOutputStream fos;
		byte[] fileData;

		try
		{
			url2 = new URL(siteFolder + "classes.dat"); //File Location goes here
			con = url2.openConnection(); // open the url connection.
			dis = new DataInputStream(con.getInputStream());
			fileData = new byte[con.getContentLength()];
			for (int q = 0; q < fileData.length; q++)
			{
				fileData[q] = dis.readByte();
			}
			InputStream is = null;
			BufferedReader bfReader = null;
			dis.close(); // close the data input stream
			fos = new FileOutputStream(new File(PATCHES_DIR, "classes.dat")); //FILE Save Location goes here
			fos.write(fileData);  // write out the file we want to save.
			fos.close(); // close the output stream writer
			log.debug("Downloaded classes.dat");
		}
		catch (Exception m)
		{
			System.out.println(m);
		}

		try
		{
			Scanner s = new Scanner(new File(PATCHES_DIR.getPath() + "\\classes.dat"));
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext())
			{
				list.add(s.next());
				log.debug("Added to list");
			}
			s.close();
			log.debug(Integer.toString(list.size()));
			// download the patches from LL
			for(String class_file : list)
			{
				log.debug("Trying to dl {}", class_file);
				File file = new File(PATCHES_DIR.getPath() + "\\" + class_file);
				file.delete();
				try
				{
					url2 = new URL(siteFolder+class_file); //File Location goes here
					con = url2.openConnection(); // open the url connection.
					dis = new DataInputStream(con.getInputStream());
					fileData = new byte[con.getContentLength()];
					for (int q = 0; q < fileData.length; q++)
					{
						fileData[q] = dis.readByte();
					}
					log.debug("Finishing reading bytes");
					InputStream is = null;
					BufferedReader bfReader = null;
					dis.close(); // close the data input stream
					fos = new FileOutputStream(new File(PATCHES_DIR, class_file)); //FILE Save Location goes here
					fos.write(fileData);  // write out the file we want to save.
					fos.close(); // close the output stream writer
					log.debug("Downloaded " + class_file);
				}
				catch (Exception m)
				{
					System.out.println(m);
				}
			}
		}
		catch (Exception e)
		{

		}
	}

	private static final int NUM_ATTEMPTS = 6;

	private ClientUpdateCheckMode updateCheckMode;
	private Object client = null;

	public ClientLoader(ClientUpdateCheckMode updateCheckMode)
	{
		this.updateCheckMode = updateCheckMode;
	}

	@Override
	public synchronized Applet get()
	{
		if (client == null)
		{
			client = doLoad();
		}

		if (client instanceof Throwable)
		{
			throw new RuntimeException((Throwable) client);
		}
		return (Applet) client;
	}

	private Object doLoad()
	{
		PATCHES_DIR.mkdirs();

		// downloads patches to patch directory
		downloadPatches();

		// set the patch folder to our runelite/patches folder
		File[] patches = PATCHES_DIR.listFiles();

		if (updateCheckMode == NONE)
		{
			return null;
		}

		try
		{
			SplashScreen.stage(0, null, "Fetching applet viewer config");

			HostSupplier hostSupplier = new HostSupplier();

			String host = null;
			RSConfig config;
			for (int attempt = 0; ; attempt++)
			{
				try
				{
					config = ClientConfigLoader.fetch(host);
					break;
				}
				catch (IOException e)
				{
					log.info("Failed to get jav_config from host \"{}\" ({})", host, e.getMessage());

					if (attempt >= NUM_ATTEMPTS)
					{
						throw e;
					}

					host = hostSupplier.get();
				}
			}

			Map<String, byte[]> zipFile = new HashMap<>();
			{
				Certificate[] jagexCertificateChain = getJagexCertificateChain();
				String codebase = config.getCodeBase();
				String initialJar = config.getInitialJar();
				HttpUrl url = HttpUrl.parse(codebase + initialJar);

				for (int attempt = 0; ; attempt++)
				{
					zipFile.clear();

					Request request = new Request.Builder()
						.url(url)
						.build();

					try (Response response = RuneLiteAPI.CLIENT.newCall(request).execute())
					{
						int length = (int) response.body().contentLength();
						if (length < 0)
						{
							length = 3 * 1024 * 1024;
						}
						final int flength = length;
						InputStream istream = new FilterInputStream(response.body().byteStream())
						{
							private int read = 0;

							@Override
							public int read(byte[] b, int off, int len) throws IOException
							{
								int thisRead = super.read(b, off, len);
								this.read += thisRead;
								SplashScreen.stage(.05, .35, null, "Downloading Old School RuneScape", this.read, flength, true);
								return thisRead;
							}
						};
						JarInputStream jis = new JarInputStream(istream);

						byte[] tmp = new byte[4096];
						ByteArrayOutputStream buffer = new ByteArrayOutputStream(756 * 1024);
						for (; ; )
						{
							JarEntry metadata = jis.getNextJarEntry();
							if (metadata == null)
							{
								break;
							}

							buffer.reset();
							for (; ; )
							{
								int n = jis.read(tmp);
								if (n <= -1)
								{
									break;
								}
								buffer.write(tmp, 0, n);
							}

							if (!Arrays.equals(metadata.getCertificates(), jagexCertificateChain))
							{
								if (metadata.getName().startsWith("META-INF/"))
								{
									// META-INF/JAGEXLTD.SF and META-INF/JAGEXLTD.RSA are not signed, but we don't need
									// anything in META-INF anyway.
									continue;
								}
								else
								{
									throw new VerificationException("Unable to verify jar entry: " + metadata.getName());
								}
							}

							zipFile.put(metadata.getName(), buffer.toByteArray());
						}
						break;
					}
					catch (IOException e)
					{
						log.info("Failed to download gamepack from \"{}\" ({})", url, e.getMessage());

						if (attempt >= NUM_ATTEMPTS)
						{
							throw e;
						}

						url = url.newBuilder().host(hostSupplier.get()).build();
					}
				}
			}

			if (updateCheckMode == AUTO)
			{
				SplashScreen.stage(.35, null, "Patching");
				Map<String, String> hashes;
				try (InputStream is = ClientLoader.class.getResourceAsStream("/patch/hashes.json"))
				{
					if (is == null)
					{
						SwingUtilities.invokeLater(() ->
							new FatalErrorDialog("The client-patch is missing from the classpath. If you are building " +
								"the client you need to re-run maven")
								.addBuildingGuide()
								.open());
						throw new NullPointerException();
					}
					hashes = new Gson().fromJson(new InputStreamReader(is), new TypeToken<HashMap<String, String>>()
					{
					}.getType());
				}

				for (Map.Entry<String, String> file : hashes.entrySet())
				{
					byte[] bytes = zipFile.get(file.getKey());

					String ourHash = null;
					if (bytes != null)
					{
						ourHash = Hashing.sha512().hashBytes(bytes).toString();
					}

					if (!file.getValue().equals(ourHash))
					{
						log.debug("{} had a hash mismatch; falling back to vanilla. {} != {}", file.getKey(), file.getValue(), ourHash);
						log.info("Client is outdated!");
						updateCheckMode = VANILLA;
						break;
					}
				}
			}

			if (updateCheckMode == AUTO)
			{
				ByteArrayOutputStream patchOs = new ByteArrayOutputStream(756 * 1024);
				int patchCount = 0;

				for (Map.Entry<String, byte[]> file : zipFile.entrySet())
				{
					byte[] bytes;
					try (InputStream is = ClientLoader.class.getResourceAsStream("/patch/" + file.getKey() + ".bs"))
					{
						if (is == null)
						{
							continue;
						}

						bytes = ByteStreams.toByteArray(is);
					}

					patchOs.reset();
					Patch.patch(file.getValue(), bytes, patchOs);
					file.setValue(patchOs.toByteArray());

					try
					{
						// output bytestream to file
						FileOutputStream fos2 = new FileOutputStream("C:/Users/Admin/Desktop/RL/" + file.getKey());
						fos2.write(patchOs.toByteArray());
						fos2.close();
					}
					catch (Exception e)
					{

					}

					// apply downloaded patches
					if (patches != null)
					{
						for (File f : patches)
						{
							if (file.getKey().equals(f.getName()))
							{
								FileInputStream is = new FileInputStream(f);
								try
								{
									bytes = IOUtils.toByteArray(is);
								}
								catch (IOException e)
								{
									e.printStackTrace();
									System.out.println("BIG ERROR!");
								}
								log.info("Applied custom patch to: {}", file.getKey());
								file.setValue(bytes);
								is.close();
							}
						}
					}
					++patchCount;
					SplashScreen.stage(.38, .45, null, "Patching", patchCount, zipFile.size(), false);
				}

				log.debug("Patched {} classes", patchCount);
			}

			SplashScreen.stage(.465, "Starting", "Starting Old School RuneScape");

			String initialClass = config.getInitialClass();

			ClassLoader rsClassLoader = new ClassLoader(ClientLoader.class.getClassLoader())
			{
				@Override
				protected Class<?> findClass(String name) throws ClassNotFoundException
				{
					String path = name.replace('.', '/').concat(".class");
					byte[] data = zipFile.get(path);
					if (data == null)
					{
						throw new ClassNotFoundException(name);
					}

					return defineClass(name, data, 0, data.length);
				}
			};

			Class<?> clientClass = rsClassLoader.loadClass(initialClass);

			Applet rs = (Applet) clientClass.newInstance();
			rs.setStub(new RSAppletStub(config));

			if (rs instanceof Client)
			{
				log.info("client-patch {}", ((Client) rs).getBuildID());
			}

			SplashScreen.stage(.5, null, "Starting core classes");

			return rs;
		}
		catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException
			| CompressorException | InvalidHeaderException | CertificateException | VerificationException
			| SecurityException e)
		{
			log.error("Error loading RS!", e);

			SwingUtilities.invokeLater(() -> FatalErrorDialog.showNetErrorWindow("loading the client", e));
			return e;
		}
	}

	private static Certificate[] getJagexCertificateChain() throws CertificateException
	{
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(ClientLoader.class.getResourceAsStream("jagex.crt"));
		return certificates.toArray(new Certificate[certificates.size()]);
	}
}
