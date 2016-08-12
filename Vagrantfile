# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "boxcutter/ubuntu1404"
  if Vagrant.has_plugin?("vagrant-cachier")
    config.cache.scope = :box
  end

  config.vm.provision "shell", inline: <<-SHELL
    cat > '/etc/profile.d/browsers.sh' <<BROWSERS
FIREFOX_VERSION=46.0.1
PHANTOMJS_VERSION=2.1.1
CHROME_VERSION=stable_current
SCREEN_WIDTH=1360
SCREEN_HEIGHT=1020
SCREEN_DEPTH=24
BROWSERS
SHELL
end
