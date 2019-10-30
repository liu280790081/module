package orange.onl_table.common.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 4740166316629191651L;
	
	private String field;
	private String type;
	private String rule;
	private String val;

	@Override
	public String toString(){
		StringBuffer sb =new StringBuffer();
		if(field == null || "".equals(field)){
			return "";
		}
		sb.append(this.field).append(" ").append(this.rule).append(" ").append(this.type).append(" ").append(this.val);
		return sb.toString();
	}
}
