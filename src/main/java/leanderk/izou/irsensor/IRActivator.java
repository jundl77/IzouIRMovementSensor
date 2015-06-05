package leanderk.izou.irsensor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.presence.provider.template.PresenceNonConstant;

/**
 * here the actual IR-related logic takes place. If the last event is over 20 minutes ago it fires a
 * @author LeanderK
 * @version 1.0
 */
public class IRActivator extends PresenceNonConstant implements GpioPinListenerDigital {
    public static final String ID = IRActivator.class.getCanonicalName();
    private final GpioController gpio;

    public IRActivator(Context context, GpioController gpio) {
        super(context, ID, true, true, true);
        this.gpio = gpio;
    }

    @Override
    public void activatorStarts() {
        debug("initializing gpio");
        String pinName = getContext().getPropertiesAssistant().getProperty("pin");
        Pin pin = null;
        if (pinName != null) {
            pin = RaspiPin.getPinByName(pinName);
            if (pin == null) {
                error("invalid pin name: " + pinName);
                pin = RaspiPin.GPIO_05;
            }
        }
        if (pin == null) {
            pin = RaspiPin.GPIO_05;
        }
        debug("pin is: " + pin.getName());
        GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(pin, "State", PinPullResistance.PULL_UP);
        if(motionSensor.isHigh()){
            debug("sensed movement");
            userEncountered();
        }
        motionSensor.addListener(this);
        stop();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        debug("sensor state changed to " + event.getState().isHigh());
        if (event.getState().isHigh()) {
            userEncountered();
        }
    }

    /**
     * Invoked when an activator-event occurs.
     *
     * @param event an instance of Event
     */
    @Override
    public void eventFired(EventModel event) {
        debug("notified");
        super.eventFired(event);
    }
}
