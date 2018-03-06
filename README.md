Projeyi çalıştırmak için iki konsol açmalısınız.

İlk konsola "MvnDebug" yazacaksınız.
İkinci konsolda önce "mvn clean install" ardından da 
"mvn hpi:run -Denforcer.skip=true" yazarak hpi dosyasını local jenkinse atmış olacaksınız.

local jenkins stop:
sudo launchctl unload /Library/LaunchDaemons/org.jenkins-ci.plist

port control:
sudo lsof -i :8080