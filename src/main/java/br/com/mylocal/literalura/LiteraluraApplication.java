package br.com.mylocal.literalura;

import br.com.mylocal.literalura.principal.Principal;
import br.com.mylocal.literalura.service.ApiService;
import br.com.mylocal.literalura.util.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	//Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		System.out.println("Iniciando busca...");

//
		Principal principal = new Principal();
		principal.startSystem();

	}
}
