package ma.azdad.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class CodeGenerator {

	static String beanName = "Vehicle";

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//		generateCode("Aaa.java");
//		generateCode("AaaRepos.java");
//		generateCode("AaaService.java");
//		generateCode("AaaView.java");
		generateCode("AaaFile.java");
		generateCode("AaaFileRepos.java");
		generateCode("AaaFileService.java");
//		generateCode("AaaHistory.java");
//		generateCode("AaaHistoryRepos.java");
//		generateCode("AaaHistoryService.java");

		/**
		 * pages
		 */

//		 generateCode("aaaList.xhtml");
//		 generateCode("aaaFooter.xhtml");
//		 generateCode("addEditAaa.xhtml");
//		 generateCode("viewAaa.xhtml");

	}

	public static void generateCode(String sourceFilepath) throws FileNotFoundException, UnsupportedEncodingException {
		String path = "files/" + sourceFilepath;
		Scanner scan = new Scanner(new File(path));

		String result = "";

		while (scan.hasNext())
			result += scan.nextLine().replace("Aaa", beanName).replace("aaa", beanName.substring(0, 1).toLowerCase() + beanName.substring(1)).replace("AAA", beanName.toUpperCase()) + "\n";

		if (!sourceFilepath.contains("xhtml"))
			System.out.println(result);

		scan.close();

		if (sourceFilepath.contains("xhtml")) {
			sourceFilepath = sourceFilepath.replace("Aaa", beanName).replace("aaa", beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
			PrintWriter writer = new PrintWriter("/home/anass/Bureau/" + sourceFilepath, "UTF-8");
			writer.print(result);
			writer.close();
		}

	}
}
