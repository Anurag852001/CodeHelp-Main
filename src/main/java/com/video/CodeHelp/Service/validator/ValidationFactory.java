package com.video.CodeHelp.Service.validator;

import com.video.CodeHelp.Enums.CodeHelpClasses;

public class ValidationFactory {

    private final IValidator defaultValidator;

    public ValidationFactory(IValidator defaultValidator){
        this.defaultValidator = defaultValidator;
    }



    public IValidator getValidator(CodeHelpClasses codeHelpClasses) {
        switch (codeHelpClasses) {
            case TestCaseGeneratorClass:
                return defaultValidator;
        }
        return  defaultValidator;
    }
}
