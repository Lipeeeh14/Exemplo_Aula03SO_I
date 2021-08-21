package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessosController {

	public ProcessosController() {}
	
	// Retorna o S.O. que está em execução na máquina
	public String os() {
		String os = System.getProperty("os.name"); // pega o nome do S.O.
		String arch = System.getProperty("os.arch"); // retorna o O.S. com mais infos
		String version = System.getProperty("os.version");
		return os + " v. "+ version +" - arch. "+arch;
	}

	public void callProcess(String process) {
		try {
			// fazendo com que a aplicação chame outro processo
			Runtime.getRuntime().exec(process);
		} catch(Exception e) {
			// Tratando a autorização de adm
			String msgErro = e.getMessage();
			if (msgErro.contains("740")){ // procura a cadeia de caracteres indicada
				//cmd /c caminho_do_processo --> chama a tela de adm
				StringBuffer buffer = new StringBuffer();
				buffer.append("cmd /c");
				buffer.append(" ");
				buffer.append(process);
				try {
					Runtime.getRuntime().exec(buffer.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				System.err.println(msgErro);
			}
		}		
	}
	
	public void readProcess(String process) {
		try {
			// Armazena o processo em uma variável
			Process p = Runtime.getRuntime().exec(process);
			// converte para fluxo de bits
			InputStream fluxo = p.getInputStream();
			// converte para string
			InputStreamReader leitor = new InputStreamReader(fluxo);
			// para não estourar a memória com os dados
			BufferedReader buffer = new BufferedReader(leitor);
			String linha = buffer.readLine();
			while(linha != null) {
				System.out.println(linha);
				linha = buffer.readLine();
			}
			buffer.close();
			leitor.close();
			fluxo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void killProcess(String param) {
		// para matar o processo por pid
		String cmdPid = "TASKKILL /PID";
		// para matar o processo por nome
		String cmdNome = "TASKKILL /IM";
		int pid = 0;
		StringBuffer buffer = new StringBuffer();
		
		//NumberFormatException --> Exception
		try {
			// TASKKILL /PID XXXX
			pid = Integer.parseInt(param);
			buffer.append(cmdPid);
			buffer.append(" ");
			buffer.append(pid);
		} catch (NumberFormatException e) {
			// TASKKILL /IM nomedoprocesso.exe
			buffer.append(cmdNome);
			buffer.append(" ");
			buffer.append(param);
		}
		
		// executa o comando
		callProcess(buffer.toString());
	}
}
