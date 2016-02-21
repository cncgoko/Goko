package goko.handlers;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class WindowCloseHandler implements IWindowCloseHandler{

    @Override
    public boolean close(MWindow window) {
        // TODO Auto-generated method stub
        Shell shell = new Shell();

        if (MessageDialog.openConfirm(shell, "Confirmation",
                "Do you want to exit?")) {
            return true;
        }
        return false;
    }
 }
