package com.jros.springboot.app.model.dao;

import java.util.List;

import com.jros.springboot.app.model.entity.Cliente;

public interface IClienteDao {

	public List<Cliente> findAll();
	public void save(Cliente cliente);
}
