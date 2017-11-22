// @author ${user}
// @date ${date}
import template from './${name}.html';

export const ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first?replace("Component", "")} = {
	template: template,
	controller: ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first?replace("Component", "")}CntlFunc
};

function ${name?replace("-", " ")?capitalize?replace(" ", "")?uncap_first?replace("Component", "")}CntlFunc() {
	
}
