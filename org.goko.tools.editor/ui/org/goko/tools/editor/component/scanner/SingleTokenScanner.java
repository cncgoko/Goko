package org.goko.tools.editor.component.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

public class SingleTokenScanner extends BufferedRuleBasedScanner {

        /**

         * Constructeur. Définit le token renvoyé.

         * @param attribute Attribut du Token.

         */

        public SingleTokenScanner(TextAttribute attribute) {

                setDefaultReturnToken(new Token(attribute));

        }

}