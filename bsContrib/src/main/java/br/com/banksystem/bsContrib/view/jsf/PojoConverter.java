package br.com.banksystem.bsContrib.view.jsf;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import br.com.banksystem.bsContrib.domain.IPojo;

@FacesConverter(value = "pojoConverter")
public class PojoConverter implements Converter {

	protected void addAttribute(final UIComponent component, final IPojo<?> o) {
		String key = "";
		if (o.getId() != null) {
			key = o.getId().toString();
		}
		getAttributesFrom(component).put(key, o);
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (value != null) { return getAttributesFrom(component).get(value); }
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		String strValue = null;
		if ((value != null) && !"".equals(value)) {
			IPojo<?> entity = (IPojo<?>) value;
			addAttribute(component, entity);

			Object codigo = entity.getId();
			if (codigo != null) { return String.valueOf(codigo); }
		}

		return strValue;
	}

	protected Map<String, Object> getAttributesFrom(final UIComponent component) {
		return component.getAttributes();
	}

}
