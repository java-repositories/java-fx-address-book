package org.example.fxml;

import org.springframework.stereotype.Component;

@Component
public class EditView extends SpringFxmlView {

        private static final String FXML_EDIT = "org/example/fxml/edit.fxml";

        public EditView() {
                super(EditView.class.getClassLoader().getResource(FXML_EDIT));
        }
}
