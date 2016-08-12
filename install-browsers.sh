#!/bin/bash -e

PKGS=""
which xvfb-run >/dev/null || PKGS="${PKGS} xvfb fontconfig"
which bzip2 >/dev/null || PKGS="${PKGS} bzip2"
which curl >/dev/null || PKGS="${PKGS} curl"

if [ -n "${PKGS}" ]; then
  apt-get update
  apt-get install -qy --force-yes ${PKGS}
fi

mkdir -p ${HOME}/.bin

if [ ! -e "${HOME}/.bin/phantomjs-${PHANTOMJS_VERSION}" ]; then
  curl --silent --show-error --location --fail --retry 3 https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-${PHANTOMJS_VERSION}-linux-x86_64.tar.bz2 | tar xjfO - phantomjs-${PHANTOMJS_VERSION}-linux-x86_64/bin/phantomjs > ${HOME}/.bin/phantomjs-${PHANTOMJS_VERSION}
  chmod +x ${HOME}/.bin/phantomjs-${PHANTOMJS_VERSION}
  ln -sf ${HOME}/.bin/phantomjs-${PHANTOMJS_VERSION} ${HOME}/.bin/phantomjs
fi

if [ ! -e "${HOME}/.bin/firefox" ]; then
  curl --silent --show-error --location --fail --retry 3 http://ftp.mozilla.org/pub/mozilla.org/firefox/releases/${FIREFOX_VERSION}/linux-x86_64/en-US/firefox-${FIREFOX_VERSION}.tar.bz2 > /tmp/firefox-${FIREFOX_VERSION}.tar.bz2
  mkdir -p /opt/firefox-${FIREFOX_VERSION}
  tar xjf /tmp/firefox-${FIREFOX_VERSION}.tar.bz2 -C /opt/firefox-${FIREFOX_VERSION}
  rm /tmp/firefox-${FIREFOX_VERSION}.tar.bz2
  echo "exec xvfb-run -a -s \"-screen 0 \${SCREEN_WIDTH:-1360}x\${SCREEN_HEIGHT:-1020}x\${SCREEN_DEPTH:-24} -ac +extension RANDR\" /opt/firefox-${FIREFOX_VERSION}/firefox/firefox \"\$@\"" > ${HOME}/.bin/firefox
  chmod +x ${HOME}/.bin/firefox
fi

if [ ! -e "${HOME}/.bin/google-chrome" ]; then
  curl --silent --show-error --location --fail --retry 3 https://dl.google.com/linux/direct/google-chrome-${CHROME_VERSION}_amd64.deb > /tmp/google-chrome-${CHROME_VERSION}_amd64.deb
  apt-get install -qy --force-yes libxss1 libappindicator1 libindicator7 libpango1.0-0 fonts-liberation xdg-utils gconf-service libasound2 libgconf-2-4 libnspr4 libnss3
  dpkg -i /tmp/google-chrome-${CHROME_VERSION}_amd64.deb
  rm /tmp/google-chrome-${CHROME_VERSION}_amd64.deb
  echo "exec xvfb-run -a -s \"-screen 0 \${SCREEN_WIDTH:-1360}x\${SCREEN_HEIGHT:-1020}x\${SCREEN_DEPTH:-24} -ac +extension RANDR\" /opt/google/chrome/google-chrome --no-sandbox \"\$@\"" > ${HOME}/.bin/google-chrome
  chmod +x ${HOME}/.bin/google-chrome
fi

