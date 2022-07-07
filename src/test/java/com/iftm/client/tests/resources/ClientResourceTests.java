package com.iftm.client.tests.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Instant;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientResourceTests {
	private int qtdClientes = 13;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// Para o teste real da aplicação iremos comentar ou retirar.
	//@MockBean
	//private ClientService service;

//	@Test
//	public void testarListarTodosClientesRetornaOKeClientes() throws Exception {
//		//configuração do meu mock
//		/*
//		List<ClientDTO> listaClientes = new ArrayList<ClientDTO>();
//		listaClientes.add(new ClientDTO(
//				new Client(7l, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0)));
//		listaClientes.add(new ClientDTO(new Client(4l, "Carolina Maria de Jesus", "10419244771", 7500.0,
//				Instant.parse("1996-12-23T07:00:00Z"), 0)));
//		listaClientes.add(new ClientDTO(
//				new Client(8l, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0)));
//		Page<ClientDTO> page = new PageImpl<ClientDTO>(listaClientes);
//		when(service.findAllPaged(any())).thenReturn(page);
//		qtdClientes = 3;
//		*/
//
//		//iremos realizar o teste
//		mockMvc.perform(get("/clients")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.content").exists())
//				.andExpect(jsonPath("$.content").isArray())
//				.andExpect(jsonPath("$.content[?(@.id =='%s')]", 7L).exists())
//				.andExpect(jsonPath("$.content[?(@.id =='%s')]", 4L).exists())
//				.andExpect(jsonPath("$.content[?(@.id =='%s')]", 8L).exists())
//				.andExpect(jsonPath("$.totalElements").value(qtdClientes));
//	}
	
	@Test
	public void testarBuscaPorIDExistenteRetornaJsonCorreto() throws Exception {
		long idExistente = 3L;
		ResultActions resultado = mockMvc.perform(get("/clients/{id}",idExistente)
				.accept(MediaType.APPLICATION_JSON));
		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.id").exists());
		resultado.andExpect(jsonPath("$.id").value(idExistente));
		resultado.andExpect(jsonPath("$.name").exists());		
		resultado.andExpect(jsonPath("$.name").value("Clarice Lispector"));
	}
	
	@Test
	public void testarBuscaPorIdNaoExistenteRetornaNotFound() throws Exception {
		long idNaoExistente = 300L;
		ResultActions resultado = mockMvc.perform(get("/clients/{id}", idNaoExistente)
				.accept(MediaType.APPLICATION_JSON));
		resultado.andExpect(status().isNotFound());
		resultado.andExpect(jsonPath("$.error").exists());
		resultado.andExpect(jsonPath("$.error").value("Resource not found"));
		resultado.andExpect(jsonPath("$.message").exists());
		resultado.andExpect(jsonPath("$.message").value("Entity not found"));
	}


	@Test
	public void testarInsertRetornaJsonCorreto() throws Exception {
		ClientDTO dto = new ClientDTO(13L, "Maria", "99999999999", 10.0, Instant.parse("2019-10-01T08:25:24.00Z"), 2);
//		String dtoString = new ObjectMapper().writeValueAsString(dto);
		String json = objectMapper.writeValueAsString(dto);
		ResultActions resultado =
				mockMvc.perform(post("/clients/")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isCreated());
		resultado.andExpect(jsonPath("$.id").exists());
		resultado.andExpect(jsonPath("$.id").value(dto.getId()));
		resultado.andExpect(jsonPath("$.name").exists());
		resultado.andExpect(jsonPath("$.name").value("Maria"));
		resultado.andExpect(jsonPath("$.cpf").exists());
		resultado.andExpect(jsonPath("$.cpf").value("99999999999"));
		resultado.andExpect(jsonPath("$.income").exists());
		resultado.andExpect(jsonPath("$.income").value(10.0));
		resultado.andExpect(jsonPath("$.children").exists());
		resultado.andExpect(jsonPath("$.children").value(2));
	}

	@Test
	public void testarUpdateRetornaJsonCorreto() throws Exception {
		ClientDTO dto = new ClientDTO(7l, "Jose", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
//		String dtoString = new ObjectMapper().writeValueAsString(dto);
		String json = objectMapper.writeValueAsString(dto);
		ResultActions resultado =
				mockMvc.perform(put("/clients/7", dto)
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.id").exists());
		resultado.andExpect(jsonPath("$.id").value(dto.getId()));
		resultado.andExpect(jsonPath("$.name").exists());
		resultado.andExpect(jsonPath("$.name").value("Jose"));
		resultado.andExpect(jsonPath("$.cpf").exists());
		resultado.andExpect(jsonPath("$.cpf").value("10239254871"));
		resultado.andExpect(jsonPath("$.income").exists());
		resultado.andExpect(jsonPath("$.income").value(5000.0));
		resultado.andExpect(jsonPath("$.children").exists());
		resultado.andExpect(jsonPath("$.children").value(0));
	}

	@Test
	public void testarUpdateRetornaIdQueNaoExiste() throws Exception {
		ClientDTO dto = new ClientDTO(7l, "Jose", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
//		String dtoString = new ObjectMapper().writeValueAsString(dto);
		String json = objectMapper.writeValueAsString(dto);
		ResultActions resultado =
				mockMvc.perform(put("/clients/14", dto)
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isNotFound());
		resultado.andExpect(jsonPath("$.error").exists());
		resultado.andExpect(jsonPath("$.error").value("Resource not found"));
	}

	@Test
	public void testarRetornoDeleteQuandoIdNaoExistir() throws Exception {
		ClientDTO dto = new ClientDTO(7l, "Jose", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
//		String dtoString = new ObjectMapper().writeValueAsString(dto);
		String json = objectMapper.writeValueAsString(dto);
		ResultActions resultado =
				mockMvc.perform(delete("/clients/14")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isNotFound());
	}

	@Test
	public void testarRetornoDeleteQuandoIdExistir() throws Exception {
		ClientDTO dto = new ClientDTO(7l, "Jose", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
//		String dtoString = new ObjectMapper().writeValueAsString(dto);
		String json = objectMapper.writeValueAsString(dto);
		ResultActions resultado =
				mockMvc.perform(delete("/clients/1")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isNoContent());
	}

	@Test
	public void testarFindByIncomeRetornaJsonCorreto() throws Exception {
		List<ClientDTO> clients = new ArrayList<>();
		JSONParser jsonParser= new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		clients.add(new ClientDTO(10L, "Chimamanda Adichie", "10114274861", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0));
		clients.add(new ClientDTO(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2));
		clients.add(new ClientDTO(9L, "Yuval Noah Harari", "10619244881", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0));
		JSONArray listJson = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(clients));
		double salarioResultado = 1500.00;
//		String dtoString = new ObjectMapper().writeValueAsString(dto);

		ResultActions resultado =
				mockMvc.perform(get("/clients/income/")
						.param("income", String.valueOf(salarioResultado))
						.accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.content").exists());
		resultado.andExpect(jsonPath("$.content").value(Matchers.containsInAnyOrder(listJson.toArray())));
		resultado.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(3));
	}

}
