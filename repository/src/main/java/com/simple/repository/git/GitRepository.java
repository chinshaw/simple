package com.simple.repository.git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import com.simple.repository.FileUtils;
import com.simple.repsository.api.IRepositoryStorage;
import com.simple.repsository.api.IVersionedRepository;

public class GitRepository implements IRepositoryStorage, IVersionedRepository {

	
	
	protected void createRepository(Path path, boolean reinialize) throws IOException {
		File gitDir = path.toFile();
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		Repository repo = repoBuilder.setGitDir(gitDir).build();
		if (repo.isBare()) {
			repo.create(true);
		}
	}
	
	
	protected void deleteRepository(Path path) {
		if (path.toFile().exists()) {
			FileUtils.deleteRecursive(path);
		}
	}
}
