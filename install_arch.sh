#!/bin/sh
echo "Installing docker"
sudo pacman -Syu docker docker-compose
sudo systemctl start docker.service
sudo systemctl enable docker.service

sudo groupadd docker
sudo usermod -aG docker "${USERNAME}"

echo "Successfully installed docker"
