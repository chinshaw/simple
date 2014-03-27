package com.simple.repsository.git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.jgit.lib.RepositoryBuilder;

import com.simple.repsository.api.IRepositoryStorage;
import com.simple.repsository.api.IVersionedRepository;

public class GitRepository implements IRepositoryStorage, IVersionedRepository {

	
	
	
	
	protected void createRepository(Path path) throws IOException {
		File gitDir = path.getFileName().toFile();
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		repoBuilder.findGitDir(gitDir).build().create();
	}
	
	
}
