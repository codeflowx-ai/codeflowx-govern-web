/*
 * Copyright (c) 2011-2020 EnartSystems.com
 *
 * This program and the accompanying materials are made available under the
 * terms of the EUROPEAN UNION PUBLIC LICENCE v. 1.2 which is available at
 * https://joinup.ec.europa.eu/collection/eupl/news/understanding-eupl-v12 Version 1.2
 * which is available at https://joinup.ec.europa.eu/sites/default/files/custom-page/attachment/eupl_v1.2_en.pdf
 * License-Identifier: EUPL-1.2
 * @author Manuel González (enartsystems) 
 */
package com.codeflowx.framework.validators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.CodeValidator;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.validator.routines.ISBNValidator;
import org.apache.commons.validator.routines.ISSNValidator;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.enartframework.core.shared.commons.CalendarUtils;
import org.enartframework.core.shared.commons.DateTimeUtil;
import org.enartframework.core.shared.commons.FormatFechas;
import org.enartframework.core.shared.commons.StringUtils;
import org.enartframework.core.shared.commons.Utilidades;
import org.enartframework.core.shared.exceptions.BaseException;
import org.enartframework.core.shared.logger.EnartLoggerFactory;
import org.enartframework.core.shared.logger.IEnartLogger;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.Binding;
import org.zkoss.bind.sys.FormBinding;
import org.zkoss.bind.sys.PropertyBinding;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;


public class BeanValidator implements Validator {
	public static final IEnartLogger log = EnartLoggerFactory.getLogger(BeanValidator.class);
	private static final long serialVersionUID = 1L;

	public static enum TYPEVALIDATOR {
		VALUES,EMAIL, URL, CCC, IBAN, NIF, CREDITCARD, NIE, PASSWORD, RETYPE, CIF, ISBN, ISSN,EAN13
	}

	final Utilidades util = new Utilidades();

	/**
	 *
	 */
	public BeanValidator() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.zkoss.bind.Validator#validate(org.zkoss.bind.ValidationContext)
	 */
	@Override
	public void validate(ValidationContext ctx) {
		
		if(ctx.getBindContext().getValidatorArg("oldValue") != null) {
			if(ctx.getBindContext().getValidatorArg("oldValue").equals(ctx.getProperty().getValue())) {
				//no se ha cambiado el valor
				return;
			}
		}
		if (ctx.getBindContext().getValidatorArg("required") != null) {
			if((Boolean) ctx.getBindContext().getValidatorArg("required")) {
				if (!isRequiredValid(ctx))
					return;
			}
		}
		if (ctx.getBindContext().getValidatorArg("equals") != null) {
			if (!validateEquals(ctx))
				return;
		}
		if (ctx.getBindContext().getValidatorArg("type") != null && ctx.getProperty().getValue() !=null && !util.isBlankOrNull(ctx.getProperty().getValue().toString())) {
			if (!validateType(ctx))
				return;
		}
		if (ctx.getBindContext().getValidatorArg("maxl") != null
				&& ctx.getBindContext().getValidatorArg("minl") != null) {
			if (!rangeSize(ctx))
				return;
		}
		if ((ctx.getBindContext().getValidatorArg("max") != null && ctx.getBindContext().getValidatorArg("min") != null)
				&& ctx.getProperty().getValue() instanceof Number) {
			if (!rangeValue(ctx))
				return;
		} else if (ctx.getBindContext().getValidatorArg("max") != null
				&& ctx.getProperty().getValue() instanceof Number) {
			if (!isValueMax(ctx))
				return;
		} else if (ctx.getBindContext().getValidatorArg("min") != null
				&& ctx.getProperty().getValue() instanceof Number) {
			if (!isValueMin(ctx))
				return;
		}
		if ((ctx.getBindContext().getValidatorArg("max") != null && ctx.getBindContext().getValidatorArg("min") != null)
				&& ctx.getProperty().getValue() instanceof Date) {
			
			if (!rangeValueDate(ctx))
				return;
		} else if (ctx.getBindContext().getValidatorArg("max") != null
				&& ctx.getProperty().getValue() instanceof Date) {
			if (!isValueMaxDate(ctx,true))
				return;
		} else if (ctx.getBindContext().getValidatorArg("min") != null
				&& ctx.getProperty().getValue() instanceof Date) {
			if (!isValueMinDate(ctx,true))
				return;
		} else if (ctx.getBindContext().getValidatorArg("min") != null
				&& ctx.getProperty().getValue() instanceof Date) {
			if (!isValueMinDate(ctx,true))
				return;
		}else if (ctx.getBindContext().getValidatorArg("expression") != null
				&& ctx.getProperty().getValue() instanceof String) {
			if (!expression(ctx))
				return;
		}
	}
   private boolean expression(ValidationContext ctx) {
	   Pattern pat = Pattern.compile(ctx.getBindContext().getValidatorArg("expression").toString());
	 	Matcher mat = pat.matcher(ctx.getProperty().getValue().toString());
	 	if(!mat.find()) {
	 		if(ctx.getBindContext().getValidatorArg("message")!=null) {
	 			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	ctx.getBindContext().getValidatorArg("message").toString());	
	 		}else {
	 			Object[] obj = new Object[1];
				obj[0] =ctx.getBindContext().getValidatorArg("expression").toString();
	 			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.expression", "No cumple con la expression", obj));
	 		}
	 	}
	 	return mat.find();	
   }
	private boolean validateEquals(ValidationContext ctx) {
		if(ctx.getProperty().getValue()!=null && ctx.getBindContext().getValidatorArg("equals")!=null){
			if(!ctx.getProperty().getValue().equals(ctx.getBindContext().getValidatorArg("equals"))) {
				Object[] obj = new Object[1];
				obj[0] ="";
				if(((Component)ctx.getBindContext().getValidatorArg("field")) instanceof Label) {
					obj[0] = ((Label)ctx.getBindContext().getValidatorArg("field")).getValue();
				}else {
					if(ctx.getBindContext().getValidatorArg("field")!=null) {
						obj[0] = ctx.getBindContext().getValidatorArg("field").toString();
					}
					
				}
				addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.equals",obj));
				return false;
			}
		}
		return true;
	}

	/**
	 * verifica si el valor es mayor
	 *
	 * @param ctx
	 * @return
	 */
	private boolean isValueMaxDate(ValidationContext ctx,boolean showError) {
		boolean valid = true;
		Locale prefer_locale = (Locale) Executions.getCurrent().getDesktop().getSession()
				.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
		// mayor de TODAY + N dias
		// mayor de una propiedad
		// mayor de una fecha
		if (ctx.getBindContext().getValidatorArg("max") instanceof String) {
			if (String.valueOf(ctx.getBindContext().getValidatorArg("max")).startsWith("TODAY+")) {
				int days = Integer.valueOf(String.valueOf(ctx.getBindContext().getValidatorArg("max")).substring(6,
						String.valueOf(ctx.getBindContext().getValidatorArg("max")).length()));
				if( CalendarUtils.isMayor((Date) ctx.getProperty().getValue(), new Date(System.currentTimeMillis()),
						days)) {
					Object[] obj = new Object[1];
					try {

						obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, days)));
						dmax=new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, days));
					} catch (Exception e) {
						log.error(e);
					}
					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.max",obj));
					return false;
				}else {
					try {
						dmax=new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, days));
					} catch (Exception e) {
						log.error(e);
					}
					return true;
				}
			} else if (String.valueOf(ctx.getBindContext().getValidatorArg("max")).startsWith("TODAY")) {
				if(CalendarUtils.isMayor((Date) ctx.getProperty().getValue(), new Date(System.currentTimeMillis()),
						null)) {
					String[] obj = new String[1];
					try {
						obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(new Date());
						dmax=new Date();
					} catch (Exception e) {
						log.error(e);
					}
					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.max",obj));
					return false;
				}
				dmax=new Date();
				return true;
			} else if (util.getGeneric().isDate(String.valueOf(ctx.getBindContext().getValidatorArg("max")),
					prefer_locale)) {
				try {
					if(CalendarUtils.isMayor((Date) ctx.getProperty().getValue(),
							FormatFechas.date(String.valueOf(ctx.getBindContext().getValidatorArg("max"))), null)) {
						String[] obj = new String[1];
						try {
							obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(ctx.getBindContext().getValidatorArg("max"));
							dmax=(Date)ctx.getBindContext().getValidatorArg("max");
						} catch (Exception e) {
							log.error(e);
						}
						if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.max",obj));

						return false;
					}
					dmax=(Date)ctx.getBindContext().getValidatorArg("max");
					return true;
				} catch (Exception e) {
					log.error(e);

					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
							Labels.getLabel("validator.date.novalid"));
					return valid = false;
				}
			} else {
				//propiedad
				if(ctx.getProperties(ctx.getBindContext().getValidatorArg("max"))!=null) {
					if(ctx.getProperties((String)ctx.getBindContext().getValidatorArg("max"))[0].getValue() instanceof Date) {
						Date max = (Date)ctx.getProperties((String)ctx.getBindContext().getValidatorArg("max"))[0].getValue();
						valid = CalendarUtils.isMayor((Date) ctx.getProperty().getValue(), max,null);
						if(!valid) {
							if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
									Labels.getLabel("validator.date.novalid"));
						}
					}
				}
			}
		}else if(ctx.getBindContext().getValidatorArg("max") instanceof Date) {
			if(CalendarUtils.isMayor((Date) ctx.getProperty().getValue(),(Date)ctx.getBindContext().getValidatorArg("max"), null)) {
				Object[] obj = new Object[1];
				try {
					obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format((Date)ctx.getBindContext().getValidatorArg("max"));
					dmax=(Date)ctx.getBindContext().getValidatorArg("max");
				} catch (Exception e) {
					log.error(e);
				}
				if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.min",obj));

				return false;
			}
			dmax=(Date)ctx.getBindContext().getValidatorArg("max");
			return true ;
		}

		return valid;
	}

	/**
	 * verifica si el valor es menor
	 *
	 * @param ctx
	 * @return
	 */
	private boolean isValueMinDate(ValidationContext ctx,boolean showError) {
		boolean valid = true;
		Locale prefer_locale = (Locale) Executions.getCurrent().getDesktop().getSession()
				.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
		// menor de TODAY + N dias
		// menor de una propiedad
		// menor de una fecha
		if (ctx.getBindContext().getValidatorArg("min") instanceof String) {
			if (String.valueOf(ctx.getBindContext().getValidatorArg("min")).startsWith("TODAY-")) {
				int days = Integer.valueOf(String.valueOf(ctx.getBindContext().getValidatorArg("min")).substring(6,
						String.valueOf(ctx.getBindContext().getValidatorArg("min")).length()));
				if(CalendarUtils.isMenor((Date) ctx.getProperty().getValue(), new Date(System.currentTimeMillis()),
						-days)) {

					Object[] obj = new Object[1];
					try {
						obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, -days)));
						dmin=new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, -days));
					} catch (Exception e) {
						log.error(e);
					}
					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.min",obj));
					return false;
				}

				try {
					dmin=new Date(new DateTimeUtil().calculaFecha(new Date().getTime(), DateTimeUtil.CALCULA_DIAS, -days));
				} catch (Exception e) {
					log.error(e);
				}
				return true ;
			} else if (String.valueOf(ctx.getBindContext().getValidatorArg("min")).startsWith("TODAY")) {
				if(CalendarUtils.isMenor((Date) ctx.getProperty().getValue(), new Date(System.currentTimeMillis()),
						null)) {
					Object[] obj = new Object[1];
					try {
						obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(new Date());
						dmin=new Date();
					} catch (Exception e) {
						log.error(e);
					}
					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.min",obj));
					return false;
				}
				dmin=new Date();
				return true;
			} else if (util.getGeneric().isDate(String.valueOf(ctx.getBindContext().getValidatorArg("min")),
					prefer_locale)) {
				try {
					if(CalendarUtils.isMenor((Date) ctx.getProperty().getValue(),
							FormatFechas.date(String.valueOf(ctx.getBindContext().getValidatorArg("min"))), null)) {
						Object[] obj = new Object[1];
						try {
							obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format(new Date());
							dmin=new Date();
						} catch (Exception e) {
							log.error(e);
						}
						if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.min",obj));

						return false;
					}
					dmin=new Date();
					return true ;
				} catch (Exception e) {
					log.error(e);
					valid = false;
					if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
							Labels.getLabel("validator.date.novalid"));
				}
			} else {
				//propiedad
				if(ctx.getProperties(ctx.getBindContext().getValidatorArg("min"))!=null) {
					if(ctx.getProperties((String)ctx.getBindContext().getValidatorArg("min"))[0].getValue() instanceof Date) {
						Date max = (Date)ctx.getProperties((String)ctx.getBindContext().getValidatorArg("min"))[0].getValue();
						valid = CalendarUtils.isMenor((Date) ctx.getProperty().getValue(), max,null);
					}
				}
			}
		}else if(ctx.getBindContext().getValidatorArg("min") instanceof Date) {
			if(CalendarUtils.isMenor((Date) ctx.getProperty().getValue(),(Date)ctx.getBindContext().getValidatorArg("min"), null)) {
				Object[] obj = new Object[1];
				try {
					obj[0] = new SimpleDateFormat("dd/MM/yyyy",prefer_locale!=null?prefer_locale: Locale.getDefault()).format((Date)ctx.getBindContext().getValidatorArg("min"));
					dmin=(Date)ctx.getBindContext().getValidatorArg("min");
				} catch (Exception e) {
					log.error(e);
				}
				if(showError)addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),	Labels.getLabel("validator.date.min",obj));

				return false;
			}
			dmin=(Date)ctx.getBindContext().getValidatorArg("min");
			return true ;
		}
		return valid;
	}

	private Date dmin;
	private Date dmax;

	/**
	 * comprueba rango de valores numericos
	 *
	 * @param ctx
	 * @return
	 */
	private boolean rangeValueDate(ValidationContext ctx) {
		boolean valid = true;
		isValueMinDate(ctx,false);
		isValueMaxDate(ctx,false);
		Object[] obj = new Object[2];
		obj[0] = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(dmin);;
		obj[1]=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dmax);;
		try {
			if(!CalendarUtils.isEquals(((Date)ctx.getProperty().getValue()), dmin)&&((Date)ctx.getProperty().getValue()).before(dmin)) {
				addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.date.range", obj));
				return false;

			}
		} catch (BaseException e) {
			log.error(e);
		}
		try {
			if(!CalendarUtils.isEquals(((Date)ctx.getProperty().getValue()), dmax)&& !((Date)ctx.getProperty().getValue()).before(dmax)) {
				addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.date.range", obj));
				return false;
			}
		} catch (BaseException e) {
			log.error(e);
		}

		return true;


	}

	/**
	 * verifica si el valor es mayor
	 *
	 * @param ctx
	 * @return
	 */
	private boolean isValueMax(ValidationContext ctx) {
		boolean valid = true;
		Number max = (Number) ctx.getBindContext().getValidatorArg("max");
		Number value = (Number) ctx.getProperty().getValue();
		if (util.getGeneric().maxValue(value.doubleValue(), max.doubleValue())) {
			Object[] obj = new Object[1];
			obj[0] = max;
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.maxvalue", obj));
			valid = false;
		}

		return valid;
	}

	/**
	 * verifica si el valor es menor
	 *
	 * @param ctx
	 * @return
	 */
	private boolean isValueMin(ValidationContext ctx) {
		boolean valid = true;
		Number min = (Number) ctx.getBindContext().getValidatorArg("min");
		Number value = (Number) ctx.getProperty().getValue();
		if (util.getGeneric().minValue(value.doubleValue(), min.doubleValue())) {
			Object[] obj = new Object[1];
			obj[0] = min;
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.minvalue", obj));
			valid = false;
		}
		return valid;
	}

	/**
	 * comprueba rango de valores numericos
	 *
	 * @param ctx
	 * @return
	 */
	private boolean rangeValue(ValidationContext ctx) {
		boolean valid = true;
		Number max = (Number) ctx.getBindContext().getValidatorArg("max");
		Number min = (Number) ctx.getBindContext().getValidatorArg("min");
		if (ctx.getProperty().getValue() instanceof Number) {
			Number value = (Number) ctx.getProperty().getValue();
			if (!util.getGeneric().isInRange(value.doubleValue(), min.doubleValue(), max.doubleValue())) {
				Object[] obj = new Object[2];
				obj[1] = max;
				obj[0] = min;

				addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.rangevalue", obj));
				valid = false;
			}
		} else {
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.rangesizetype"));
			valid = false;
		}
		return valid;
	}

	/**
	 * comprueba el rango del tamano del texto
	 *
	 * @param ctx
	 * @return
	 */
	private boolean rangeSize(ValidationContext ctx) {
		boolean valid = true;
		Number maxLength = (Number) ctx.getBindContext().getValidatorArg("maxl");
		if (ctx.getProperty().getValue() instanceof String) {
			String value = (String) ctx.getProperty().getValue();
			if (value.length() > maxLength.longValue()) {
				Object[] obj = new Object[2];
				obj[1] = (Number) ctx.getBindContext().getValidatorArg("maxl");
				obj[0] = (Number) ctx.getBindContext().getValidatorArg("minl");

				addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
						Labels.getLabel("error.system.maxlength", obj));
				valid = false;
			}
		} else {
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.rangesizetype"));
			valid = false;
		}
		return valid;
	}

	/**
	 * comprueba si el campo no es null o vacio
	 *
	 * @param ctx
	 * @return
	 */
	private boolean isRequiredValid(ValidationContext ctx) {
		if (ctx.getProperty().getValue()==null || util.isBlankOrNull(String.valueOf(ctx.getProperty().getValue()))) {
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(), Labels.getLabel("validator.required"));
			return false;
		}
		return true;
	}

	/**
	 * valida el tipo de valor recibido
	 *
	 * @param ctx
	 * @return
	 */
	private boolean validateType(ValidationContext ctx) {
		boolean valid = false;
		if (isTypeValidate((String) ctx.getBindContext().getValidatorArg("type"))) {
			String type = (String) ctx.getBindContext().getValidatorArg("type");
			if (type.equalsIgnoreCase(TYPEVALIDATOR.EMAIL.name())) {
				valid = util.isValidEmail(String.valueOf(ctx.getProperty().getValue()));
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CCC.name())) {
				valid = false;
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.NIE.name())) {
				valid = util.compruebaNie(String.valueOf(ctx.getProperty().getValue()));
				;
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.NIF.name())) {
				valid = util.compruebaNif(String.valueOf(ctx.getProperty().getValue()));
				;
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.PASSWORD.name())) {
				valid = String.valueOf(ctx.getProperty().getValue()).matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
				if(!valid) {
					Object[] obj = new Object[1];
					obj[0] = (String) ctx.getBindContext().getValidatorArg("type");
					addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
							Labels.getLabel("validator.type.novalid.password", obj));	
					return false;
				}
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.URL.name())) {
				valid = util.isValidURL(String.valueOf(ctx.getProperty().getValue()));
				;
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CIF.name())) {
				valid = util.compruebaCIF(String.valueOf(ctx.getProperty().getValue()));
				;
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CREDITCARD.name())) {
				CreditCardValidator ccv = new CreditCardValidator(CreditCardValidator.AMEX + CreditCardValidator.VISA
						+ CreditCardValidator.DINERS + CreditCardValidator.MASTERCARD);
				valid = ccv.isValid(String.valueOf(ctx.getProperty().getValue()));
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.ISBN.name())) {
				valid = ISBNValidator.getInstance().isValid(String.valueOf(ctx.getProperty().getValue()));
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.IBAN.name())) {
				valid = IBANValidator.getInstance().isValid(String.valueOf(ctx.getProperty().getValue()));
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.ISSN.name())) {
				valid = ISSNValidator.getInstance().isValid(String.valueOf(ctx.getProperty().getValue()));
			} else if (type.equalsIgnoreCase(TYPEVALIDATOR.EAN13.name())) {
				 CodeValidator validator = new CodeValidator("^[0-9]*$", 13, EAN13CheckDigit.EAN13_CHECK_DIGIT);
				valid = validator.isValid(String.valueOf(ctx.getProperty().getValue()));
				
				
			}else if(type.equalsIgnoreCase(TYPEVALIDATOR.VALUES.name())) {
				String values = (String) ctx.getBindContext().getValidatorArg("values");
				StringTokenizer st = new StringTokenizer(values, ",");
				while(st.hasMoreTokens()) {
					if(st.nextToken().equals((String)ctx.getProperty().getValue())){
						return true;
					}
				}
				if(!valid) {
					Object[] obj = new Object[1];
					obj[0] = values;
					if(ctx.getBindContext().getValidatorArg("field")!=null) {
						obj[0] = ctx.getBindContext().getValidatorArg("field");
					}
					
					addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
							Labels.getLabel("validator.values", obj));
				}

			}
		} else {
			Object[] obj = new Object[1];
			obj[0] = (String) ctx.getBindContext().getValidatorArg("type");
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
					Labels.getLabel("validator.type.novalid", obj));
			return false;
		}
		if (!valid) {
			Object[] obj = new Object[1];
			obj[0] = (String) ctx.getBindContext().getValidatorArg("type");
			addInvalidMessage(ctx, ctx.getBindContext().getComponent().getId(),
					Labels.getLabel("validator.novalid", obj));
		}
		return valid;
	}

	/**
	 * verifica que el tipo de campo se puede validar
	 *
	 * @param type
	 * @return
	 */
	private boolean isTypeValidate(String type) {
		boolean valid = false;
		if (type.equalsIgnoreCase(TYPEVALIDATOR.EMAIL.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CCC.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.NIE.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.NIF.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.PASSWORD.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.URL.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CIF.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.CREDITCARD.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.ISBN.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.ISSN.name())) {
			valid = true;
		} else if (type.equalsIgnoreCase(TYPEVALIDATOR.IBAN.name())) {
			valid = true;
		}  else if (type.equalsIgnoreCase(TYPEVALIDATOR.EAN13.name())) {
			valid = true;
		} else if(type.equalsIgnoreCase(TYPEVALIDATOR.VALUES.name())) {
			valid = true;
		}

		return valid;

	}

	/**
	 * add a message to validation context, when you call this method, it also set
	 * context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param message
	 *            the message of validation
	 */
	protected void addInvalidMessage(ValidationContext ctx, String message) {
		addInvalidMessages(ctx, null, new String[] { message });
	}

	/**
	 * add a message to validation context, when you call this method, it also sets
	 * context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param key
	 *            the custom key of message
	 * @param message
	 *            the message of validation
	 */
	protected void addInvalidMessage(ValidationContext ctx, String key, String message) {
		addInvalidMessages(ctx, key, new String[] { message });
	}

	/**
	 * add a message to validation context, when you call this method, it also sets
	 * context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param key
	 *            the custom key of message
	 * @param message
	 *            the message of validation
	 * @param value
	 *            the value of the rejected field
	 * @since 8.0.1
	 */
	protected void addInvalidMessage(ValidationContext ctx, String key, String message, Object value) {
		addInvalidMessages(ctx, key, new String[] { message }, value);
	}

	/**
	 * add multiple messages to validation context, when you call this method, it
	 * also sets the context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param messages
	 *            messages of validation
	 */
	protected void addInvalidMessages(ValidationContext ctx, String[] messages) {
		addInvalidMessages(ctx, null, messages);
	}

	/**
	 * add multiple messages to validation context, when you call this method, it
	 * also sets the context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param key
	 *            the custom key of message
	 * @param messages
	 *            messages of validation
	 */
	protected void addInvalidMessages(ValidationContext ctx, String key, String[] messages) {
		addInvalidMessages(ctx, key, messages, null);
	}

	/**
	 * add multiple messages to validation context, when you call this method, it
	 * also sets the context invalid.
	 *
	 * @param ctx
	 *            the validation context
	 * @param key
	 *            the custom key of message
	 * @param messages
	 *            messages of validation
	 * @param value
	 *            the value of the rejected field
	 * @since 8.0.1
	 */
	protected void addInvalidMessages(ValidationContext ctx, String key, String[] messages, Object value) {
		ctx.setInvalid();
		ValidationMessages vmsgs = ((BinderCtrl) ctx.getBindContext().getBinder()).getValidationMessages();
		if (vmsgs != null) {
			Binding binding = ctx.getBindContext().getBinding();
			String attr = null;
			if (binding instanceof PropertyBinding) {
				attr = ((PropertyBinding) binding).getFieldName();
			} else if (binding instanceof FormBinding) {
				attr = ((FormBinding) binding).getFormId();
			} else {
				// ignore children binding;
			}
			if (attr != null) {
				vmsgs.addMessages(ctx.getBindContext().getComponent(), attr, key, messages, value);
			}
		} else {
			log.warn("ValidationMessages not found on binder " + ctx.getBindContext().getBinder() + ", please init it");
		}
	}
}