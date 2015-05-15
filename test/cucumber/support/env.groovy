import books.Book
import geb.binding.BindingUpdater
import geb.Browser
import grails.plugin.remotecontrol.RemoteControl

this.metaClass.mixin(cucumber.api.groovy.Hooks)

Before() {
    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()
}

After () {
    bindingUpdater.remove ()

    /*
    def remote = new RemoteControl()
    remote {
        Book.deleteAll(Book.findAll())
    }
    */
}
