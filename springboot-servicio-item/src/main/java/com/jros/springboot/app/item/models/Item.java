package com.jros.springboot.app.item.models;

public class Item {
	
	private Producto producto;
	private Integer cantidad;
	
	
	public Item() {}
	 
	
	public Item(Producto producto, Integer cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	@Override
	public String toString() {
		return "Item [producto=" + producto + ", cantidad=" + cantidad + "]";
	}
	
	/**Metodo para calcular el importe precio por catidad.
	 * 
	 * @return total de precio por cantidad
	 */
	public Double gettotal() {
		return producto.getPrecio() * cantidad.doubleValue();
	}
	
  
}
