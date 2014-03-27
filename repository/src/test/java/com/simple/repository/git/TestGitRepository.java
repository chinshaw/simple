package com.simple.repository.git;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

public class TestGitRepository {

	final Path testPath = Paths.get("/tmp/testrepo");
	final GitRepository repo = new GitRepository();

	@After
	public void cleanup() {
		repo.deleteRepository(testPath);
	}

	@Test
	public void testCreateRepository() throws IOException {
		repo.createRepository(testPath, true);
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateRepositoryAgainFails() throws IOException {
		repo.createRepository(testPath, true);
		try {
			repo.createRepository(testPath, true);
		} finally {
			repo.deleteRepository(testPath);
		}
	}

	@Test
	public void testDeleteRepository() throws IOException {
		try {
			repo.createRepository(testPath, true);
		} finally {
			repo.deleteRepository(testPath);
		}
		if (testPath.toFile().exists()) {
			fail("Could not delete repository path");
		}
	}

	@Test
	public void testCreateScript() {
		
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
