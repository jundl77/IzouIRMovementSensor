package leanderk.izou.irsensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.intellimate.izou.activator.ActivatorModel;
import org.intellimate.izou.events.EventsControllerModel;
import org.intellimate.izou.output.OutputExtensionModel;
import org.intellimate.izou.output.OutputPluginModel;
import org.intellimate.izou.sdk.addon.AddOn;
import org.intellimate.izou.sdk.contentgenerator.ContentGenerator;
import ro.fortsoft.pf4j.Extension;

/**
 * the addon-class
 * @author LeanderK
 * @version 1.0
 */
@Extension
public class IRAddon extends AddOn {
    public static final String ID = IRAddon.class.getCanonicalName();
    GpioController gpio;

    public IRAddon() {
        super(ID);
    }

    @Override
    public void prepare() {
        gpio = GpioFactory.getInstance();
    }

    @Override
    public ActivatorModel[] registerActivator() {
        ActivatorModel[] activatorModels = new ActivatorModel[1];
        activatorModels[0] = new IRActivator(getContext(), gpio);
        return activatorModels;
    }

    @Override
    public ContentGenerator[] registerContentGenerator() {
        return new ContentGenerator[0];
    }

    @Override
    public EventsControllerModel[] registerEventController() {
        return new EventsControllerModel[0];
    }

    @Override
    public OutputPluginModel[] registerOutputPlugin() {
        return new OutputPluginModel[0];
    }

    @Override
    public OutputExtensionModel[] registerOutputExtension() {
        return new OutputExtensionModel[0];
    }
}
