package com.struts2.bolsadeideas.video.struts2.cursos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class CursoAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private List<String> cursos;
	public static final String TITULO="Algun titulo importante"; 

	@Override
	public String execute() throws Exception {
		
		cursos= new ArrayList<String>();
		cursos.add("Spring");
		cursos.add("Struts2");
		cursos.add("Java");
		cursos.add("Javascript");
		cursos.add("GO");
		
		return SUCCESS;
	}

	public List<String> getCursos() {
		return cursos;
	}


	public static String getTitulo() {
		return TITULO;
	}
	
	public Date getFecha() {
		return new Date();
	}

}
