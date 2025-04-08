# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
    config.vm.box = "ubuntu/focal64"
    
    # Désactiver le partage de dossier inutile
    config.vm.synced_folder ".", "/vagrant", disabled: true  
  
    # Créer 3 VMs (node2, node3, node4) pour laisser 192.168.60.1 à l'hôte
    (2..5).each do |i|
      config.vm.define "node#{i}" do |node|
        node.vm.hostname = "node#{i}"
        
        # Configuration host-only (l'hôte sera automatiquement dans le même LAN)
        node.vm.network "private_network",
          ip: "192.168.60.#{i}",
          netmask: "255.255.255.0",
          type: "hostonly"  # <- Ceci est la clé pour inclure l'hôte
        
        # Configuration VirtualBox
        node.vm.provider "virtualbox" do |vb|
          vb.memory = 1024
          vb.cpus = 1
          vb.name = "node-#{i}"
        end
      end
    end
  end