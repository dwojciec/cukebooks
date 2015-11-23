package steps.books

import books.pages.ListPage
import com.saucelabs.saucerest.SauceREST
import cucumber.api.Scenario
import geb.binding.BindingUpdater
import geb.Browser
import grails.plugin.remotecontrol.RemoteControl
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.remote.RemoteWebDriver

this.metaClass.mixin(cucumber.api.groovy.Hooks)

void embedScreenshot(Scenario scenario) {
    try {
        final byte[] screenshot = ((TakesScreenshot) browser.driver).getScreenshotAs(OutputType.BYTES)
        scenario.embed(screenshot, "image/png")
    } catch (Exception e) {
        scenario.write("Could not embed screenshot: ${e.toString()}")
    }
}

void embedHTML(Scenario scenario) {
    try {
        String html = browser.driver.findElement(By.cssSelector("html")).getAttribute("innerHTML")
        scenario.embed(StringEscapeUtils.escapeHtml4(html).getBytes("UTF-8"), "text/plain;charset=UTF-8")
    } catch (Exception e) {
        scenario.write("Could not embed HTML: ${e.toString()}")
    }
}

SauceREST getSauceREST() {
    browser.config.rawConfig.sauceREST ?: null
}

String getSauceJobId() {
    ((RemoteWebDriver) browser.driver).getSessionId() as String
}


Before() { Scenario scenario ->
    bindingUpdater = new BindingUpdater (binding, new Browser ())
    bindingUpdater.initialize ()

    binding.remote = new RemoteControl()

    getSauceREST()?.updateJobInfo(getSauceJobId(), ["name": "CukeBooks "+scenario.name])
}

After () { Scenario scenario ->
    if (scenario.isFailed()) {
        embedScreenshot(scenario)
        embedHTML(scenario)
        getSauceREST()?.jobFailed(getSauceJobId())
    } else {
        // We don't want to do this when using one job for all tests, else we lose the fact that
        // something failed
        //getSauceREST()?.jobPassed(getSauceJobId())
    }

    browser.clearCookiesQuietly()
    // FIXME: we need to move off an authenticated page, such as overview, for if we go there next it won't fail
    try { to ListPage } catch (e) { }

    bindingUpdater.remove ()
}
