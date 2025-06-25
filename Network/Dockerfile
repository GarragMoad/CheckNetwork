# Utiliser une image de base légère avec Python
FROM python:3.10-slim

# Mise à jour des paquets et installation des dépendances nécessaires
RUN apt-get update && apt-get install -y \
    bash\
    ssh \
    sshpass \
    && rm -rf /var/lib/apt/lists/*

# Installer Ansible via pip
RUN pip install --no-cache-dir ansible

# Créer un dossier de travail pour Ansible
WORKDIR /ansible

# Copier les clés dans l'image
COPY ssh_keys /root/.ssh/keys

# Fixer les permissions des clés (très important pour SSH)
RUN chmod 700 /root/.ssh && \
    chmod 600 /root/.ssh/keys/*

# Par défaut, la commande qui sera exécutée est un shell interactif
ENTRYPOINT ["/bin/bash"]