package framework.cucumber.report.utils;

import java.io.File;

import framework.cucumber.report.model.ReportFileStructure;

public class GetFileStructure {

	public static File get(ReportFileStructure fileStructure, String directory, String fileName) {
		if (fileStructure.getUserInfo() == null || fileStructure.getGroup() == null) {
			// diretorio padrao
			return new File("./" + directory + File.separator + fileName);
		} else {
			String cenario = fileStructure.getFunctionality().replaceAll(fileStructure.getGroup(), "");
			if (cenario.equals("")) {
				cenario = "";
			} else {
				cenario += File.separator;
			}
			String nameAndId = fileStructure.getUserInfo();
			String nomePasta = fileStructure.getGroup() + File.separator + cenario + nameAndId;

			criaPastas("./", directory + File.separator + nomePasta);

			return new File("./" + directory + File.separator + nomePasta + File.separator + fileName);
		}
	}

	private static void criaPastas(String caminhoInicial, String caminhopastas) {
		File file = null;
		String[] pastas = caminhopastas.split(File.separator);
		for (String pasta : pastas) {
			caminhoInicial += pasta;
			file = new File(caminhoInicial);
			if (!file.exists()) {
				file.mkdir();
			}
			caminhoInicial += File.separator;
		}
	}
}
