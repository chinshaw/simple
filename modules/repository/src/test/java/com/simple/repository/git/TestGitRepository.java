package com.simple.repository.git;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Test;

public class TestGitRepository {

	final Path testPath = Paths.get("/tmp/testrepo");
	final GitRepository repo = new GitRepository(testPath);

	@After
	public void cleanup() {
		repo.deleteRepository();
	}

	@Test
	public void testCreateRepository() throws IOException {
		repo.initialize(true);
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateRepositoryAgainFails() throws IOException {
		repo.initialize(true);
		try {
			repo.initialize(true);
		} finally {
			repo.deleteRepository();
		}
	}

	@Test
	public void testDeleteRepository() throws IOException {
		try {
			repo.initialize( true);
		} finally {
			repo.deleteRepository();
		}
		if (testPath.toFile().exists()) {
			fail("Could not delete repository path");
		}
	}

	@Test
	public void testCreateScript() throws IOException, GitAPIException {
		repo.initialize(true);
		String filename = "donkey";
		String contents = "what shrek?";
		repo.add(filename, contents.getBytes());
		
		byte[] bytes = repo.get(filename);
		
		assert(contents.equals(new String(bytes)));
	}
	
	@Test
	public void testDeleteScript() {
		
	}

	@Test
	public void testFetchScript() {

	}

	@Test
	public void testChangeScript() {

	}

	@Test
	public void testListVersions() {

	}

	@Test
	public void testFetchVersion() {

	}

	@Test
	public void changeVersion() {

	}
}
