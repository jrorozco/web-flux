package mc.com.jros.microservicios.app.cursos.services;

import org.springframework.stereotype.Service;

import mc.com.jros.microservicios.app.cursos.models.entity.Curso;
import mc.com.jros.microservicios.app.cursos.models.repository.CursoRepository;
import mc.com.jros.microservicios.commons.services.CommonServiceImpl;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, CursoRepository>  implements CursoService{

	

}
