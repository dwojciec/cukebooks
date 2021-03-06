import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities
import io.github.bonigarcia.wdm.ChromeDriverManager
import io.github.bonigarcia.wdm.MarionetteDriverManager
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver

import java.util.regex.Pattern

/**
 * Set common properties for WebDriver instances.
 */
def postProcessDriver = { WebDriver driver, Map caps = [:] ->
    int width = 1280, height = 1024
    if (caps['width'] && caps['height']) {
        width = caps['width'] as int
        height = caps['height'] as int
    }
    if (width > 0 && height > 0) {
        driver.manage().window().setSize(new Dimension(width, height))
    }

    driver
}

// Try to find phantomjs
if (!System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY)) {
    def binary = (System.getenv('PATH') ?: '').split(Pattern.quote(File.pathSeparator))
      .collect { new File(new File(it), 'phantomjs') }
      .find { it.exists() }
    if (binary) {
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, binary.absolutePath)
    }
}

environments {
  phantomjs {
    driver = { postProcessDriver.call(new PhantomJSDriver(new DesiredCapabilities())) }
  }
  firefox {
    driver = {
      MarionetteDriverManager.getInstance().setup()
      DesiredCapabilities capabilities = DesiredCapabilities.firefox()
      capabilities.setCapability("marionette", true)
      postProcessDriver.call(new FirefoxDriver(capabilities))
   }
  }
  chrome {
    driver = {
      ChromeDriverManager.getInstance().setup()
      postProcessDriver.call(new ChromeDriver())
    }
  }
  remote {
    driver = {
      def map = System.getProperty('geb.browser', '').split(',').collectEntries { it.split('=') as List }
      DesiredCapabilities caps = new DesiredCapabilities(map)
      postProcessDriver.call(new RemoteWebDriver(new URL(System.getProperty('geb.url')), caps))
    }
  }
}

