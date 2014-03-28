package com.simple.repository.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import com.simple.repository.FileUtils;
import com.simple.repsository.api.IRepositoryStorage;
import com.simple.repsository.api.IVersionedRepository;

public class GitRepository implements IRepositoryStorage, IVersionedRepository {

	private final Path repoPath;

	public GitRepository(Path repoPath) {
		this.repoPath = repoPath;
	}

	protected void initialize(boolean reinialize) throws IOException {
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		repoBuilder.setGitDir(repoPath.toFile()).build().create();
	}

	protected void deleteRepository() {
		if (repoPath.toFile().exists()) {
			FileUtils.deleteRecursive(repoPath);
		}
	}

	public void add(final String fileName, final byte[] contents) throws IOException, NoFilepatternException, GitAPIException {
		// String tmpDir = System.getProperty("java.io.tmpdir");
		File tmpFile = new File(git().getRepository().getWorkTree(), fileName);
		Files.write(tmpFile.toPath(), contents);
		git().add().addFilepattern(tmpFile.getPath()).call();
	}

	public byte[] get(final String filename) throws IOException, GitAPIException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectId head = repo().resolve("HEAD");
		git().archive().setFilename(filename).setTree(head).setOutputStream(baos).call();
		return baos.toByteArray();
	}

	private Git git() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		return Git.cloneRepository().setDirectory(new File("/tmp/tmpdir1")).setURI(repoPath.toString()).call();
	}

	private Repository repo() throws IOException {
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		return repoBuilder.setGitDir(repoPath.toFile()).build();
	}
}
