package br.com.fiap.seacare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@Controller
@OpenAPIDefinition(
	info = @Info(
		title = "API do SeaCare",
		description = "App de denúncias de poluição marinha em encostas e praias",
		contact = @Contact(name = "Igor Miguel Silva", email = "rm99495@fiap.com.br"),
		version = "1.0.0"
	)
)
public class SeacareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeacareApplication.class, args);
	}

	@RequestMapping
	@ResponseBody
	public String home(){
		return "SeaCare";
	}

}
