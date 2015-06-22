package steps.books

import geb.binding.BindingUpdater
import geb.Browser
import grails.plugin.remotecontrol.RemoteControl

this.metaClass.mixin(cucumber.api.groovy.Hooks)

Before() {
    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()

    binding.remote = new RemoteControl()
}

After () {
    bindingUpdater.remove ()
}
