package com.video.CodeHelp.Service.validator;

import com.video.CodeHelp.Enums.CodeHelpClasses;
import com.video.CodeHelp.Service.validator.impl.DefaultValidator;
import jakarta.inject.Inject;

public class ValidationFactory {

    private final IValidator defaultValidator;

    @Inject
    public ValidationFactory(DefaultValidator defaultValidator){
        this.defaultValidator = defaultValidator;
    }



    public IValidator getValidator(CodeHelpClasses codeHelpClasses) {
        switch (codeHelpClasses) {
            case TestCaseGeneratorClass:
            case Default:
                return defaultValidator;
        }
        return  defaultValidator;
    }
}
