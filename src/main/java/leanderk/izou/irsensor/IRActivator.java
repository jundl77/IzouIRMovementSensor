package leanderk.izou.irsensor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.presence.provider.template.PresenceNonConstant;

/**
 * here the actual IR-related logic takes place. If the last event is over 20 minutes ago it fires a
 * @author LeanderK
 * @version 1.0
 */
public class IRActivator extends PresenceNonConstant implements GpioPinListenerDigital {
    public static final String ID = IRActivator.class.getCanonicalName();

    public IRActivator(Context context, GpioController gpio) {
        super(context, ID, true);
        GpioController gpio1 = gpio;
        String pinName = getContext().getPropertiesAssistant().getProperties("pin");
        Pin pin = null;
        if (pinName != null) {
            pin = RaspiPin.getPinByName(pinName);
            if (pin == null)
                error("invalid pin name: " + pinName);
        }
        if (pin == null) {
            pin = RaspiPin.GPIO_05;
        }
        GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(pin, "State", PinPullResistance.PULL_UP);
        if(motionSensor.isHigh()){
            userEncountered();
        }
    }

    @Override
    public void activatorStarts() {
        stop();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        if (event.getState().isHigh()) {
            userEncountered();
        }
    }
}
