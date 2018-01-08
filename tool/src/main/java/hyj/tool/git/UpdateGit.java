package hyj.tool.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * JGit API测试
 */
public class UpdateGit {
	/** 当前用户名  **/
	private String userName;
	/** 当前密码 **/
	private String password;

	/**
	 * 克隆远程库
	 * 
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public void testClone(String remotePath, String remoteBranchName, String localGitPath) throws IOException, GitAPIException {
		// 设置远程服务器上的用户名和密码
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
				userName, password);

		// 克隆代码库命令
		CloneCommand cloneCommand = Git.cloneRepository();

		cloneCommand.setURI(remotePath) 	// 设置远程URI
//				    .setBranch(remoteBranchName) 		// 设置clone下来的分支
				    .setDirectory(new File(localGitPath)) 	// 设置下载存放路径
				    .setCredentialsProvider(usernamePasswordCredentialsProvider) // 设置权限验证
				    .call();
		
	}

	/**
	 * 拉取远程仓库内容到本地
	 */
	public void testPull(String remotePath, String remoteBranchName, String localGitPath) throws IOException, GitAPIException {

		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
				userName, password);
		
		// git仓库地址
		@SuppressWarnings("resource")
		Git git = new Git(new FileRepository(localGitPath + "/.git"));
		 
		//获取远程更新的信息
		git.pull().setCredentialsProvider(usernamePasswordCredentialsProvider)
		          .call();
		
		//如果当前仓库有这条分支，那么直接切换，否则新增
		if(git.getRepository().getRef(remoteBranchName) != null){
			git.checkout().setName(remoteBranchName).setForce(true).call();
		}else{
			git.checkout().setName(remoteBranchName).setCreateBranch(true).setForce(true).setStartPoint("remotes/origin/"  + remoteBranchName).call();
		}
		
		//获取远程分支的信息
		git.pull().setRemoteBranchName(remoteBranchName)
				  .setCredentialsProvider(usernamePasswordCredentialsProvider)
				  .call();
	}
	

}