package edu.ceti.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String credencial;
	private String nombres;
	private String apellido_pat;
	private String apellido_mat;
	private String estado;
	private String municipio;
	private String codigo_postal;
	private String colonia;
	private char tipo;
	private Boolean vacaciones;
	private String telefono;
	private Integer edad;
	private Boolean genero;

	@Temporal(TemporalType.DATE)
	private Date fecha_ingreso;

	@Temporal(TemporalType.DATE)
	private Date fecha_inicio;

	@Temporal(TemporalType.DATE)
	private Date fecha_pago;

	private Integer semana_pago;

	public Alumno() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}
	
	

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellido_pat() {
		return apellido_pat;
	}

	public void setApellido_pat(String apellido_pat) {
		this.apellido_pat = apellido_pat;
	}

	public String getApellido_mat() {
		return apellido_mat;
	}

	public void setApellido_mat(String apellido_mat) {
		this.apellido_mat = apellido_mat;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCodigo_postal() {
		return codigo_postal;
	}

	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public char getTipo() {
		return tipo;
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	public Boolean getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(Boolean vacaciones) {
		this.vacaciones = vacaciones;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Boolean getGenero() {
		return genero;
	}

	public void setGenero(Boolean genero) {
		this.genero = genero;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_pago() {
		return fecha_pago;
	}

	public void setFecha_pago(Date fecha_pago) {
		this.fecha_pago = fecha_pago;
	}

	public Integer getSemana_pago() {
		return semana_pago;
	}

	public void setSemana_pago(Integer semana_pago) {
		this.semana_pago = semana_pago;
	}

	private static final long serialVersionUID = -5956388327024477726L;

	@Override
	public String toString() {
		return "Alumno [id=" + id + ", credencial=" + credencial + ", nombres=" + nombres + ", apellido_pat="
				+ apellido_pat + ", apellido_mat=" + apellido_mat + ", estado=" + estado + ", municipio=" + municipio
				+ ", codigo_postal=" + codigo_postal + ", colonia=" + colonia + ", tipo=" + tipo + ", vacaciones="
				+ vacaciones + ", telefono=" + telefono + ", edad=" + edad + ", genero=" + genero + ", fecha_ingreso="
				+ fecha_ingreso + ", fecha_inicio=" + fecha_inicio + ", fecha_Pago=" + fecha_pago + ", semana_pago="
				+ semana_pago + "]";
	}

	
	
	
	

}
