package com.codeflowx.framework.validators;

import org.apache.commons.lang3.SerializationUtils;
import org.enartframework.nocode.datamodel.model.Entity;
import org.enartframework.nocode.datamodel.model.Field;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.zk.utils.bind.BeanValidator;
import org.zkoss.bind.ValidationContext;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author manuel
 *
 */
@Setter
@Getter
@Slf4j

public class UniqueValidator extends BeanValidator {
	@NonNull
	private Object beanData;
	@NonNull
	private BusinessService businessService;
	
	
	
	@Override
	/**
	 *  su recibe dos argumentos oldValue valor actual del bean y field nombre del campo unico a validar 
	 */
	public void validate(ValidationContext ctx) {
		if(ctx.getProperty().getValue()!=null && ctx.getBindContext().getValidatorArg("oldValue")!=null) {
			if(ctx.getProperty().getValue().equals(ctx.getBindContext().getValidatorArg("oldValue"))) {
				return;
			}
		}
		if(ctx.getBindContext().getValidatorArg("field") != null) {
			try {
				if( businessService.existValue(beanData,ctx.getBindContext().getValidatorArg("field").toString(),ctx.getProperty().getValue())) {
					addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
							Labels.getLabel("validator.duplicate", "el valor introducido ya existe"));
				}else {
					return;
				}
			} catch (DaoException e) {
				Clients.showNotification(e.getMessage(), "alert", null, "before_center", 60);
			}
		}else {
			super.validate(ctx);
		}
		
	}

	public UniqueValidator(@NonNull Object beanData, @NonNull BusinessService businessService) {
		super();
		this.beanData = beanData;
		this.businessService = businessService;
		
	}
}
