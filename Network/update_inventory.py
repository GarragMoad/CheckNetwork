import json
import subprocess

# === Chemins ===
TARGETS_FILE = "C:\\Users\\garra\\Documents\\solarwinds\\Network\\Targets\\targets.json"
ANSIBLE_INVENTORY ="C:\\Users\\garra\\Documents\\solarwinds\\Network\\inventory.ini"
PING_YML = "C:\\Users\\garra\\Documents\\solarwinds\\Prometheus\\prometheus\\ping_config\\config.yml"
PROMETHEUS_YML = "C:\\Users\\garra\\Documents\\solarwinds\\Prometheus\\prometheus\\prometheus.yml"

prometheus_compose_file = "C:\\Users\\garra\\Documents\\solarwinds\\Prometheus\\prometheus\\docker-compose.yml"
ansible_compose_file = "C:\\Users\\garra\\Documents\\solarwinds\\Network\\docker-compose.yml"

# === Chargement des cibles ===
with open(TARGETS_FILE, "r") as f:
    nodes = json.load(f)

# === Générer le fichier Ansible inventory.ini ===
with open(ANSIBLE_INVENTORY, "w") as f:
    f.write("[snmp_devices]\n")
    for node in nodes:
        key_path = f"/root/.ssh/{node['hostname']}"
        f.write(f"{node['hostname']} ansible_host={node['ip']} ansible_user=vagrant ansible_ssh_private_key_file={key_path}\n")

# === Générer le ping.yml ===
with open(PING_YML, "w") as f:
    f.write("""ping:
  interval: 5s
  timeout: 3s
  payload_size: 56
  history_size: 20
  workers: 5
  privileged: false

targets:\n""")
    for node in nodes:
        f.write(f"  - {node['ip']}\n")

# === Générer prometheus.yml pour SNMP Exporter ===
with open(PROMETHEUS_YML, "w") as f:
    f.write("""global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'snmp'
    metrics_path: /snmp
    params:
      module: [if_mib]
    static_configs:
      - targets:\n""")
    for node in nodes:
        f.write(f"          - {node['ip']}\n")
    f.write("""    relabel_configs:
      - source_labels: [__address__]
        target_label: __param_target
      - source_labels: [__param_target]
        target_label: instance
      - target_label: __address__
        replacement: snmp-exporter:9116

  - job_name: 'ping'
    static_configs:
      - targets: ['ping-exporter:9427']\n""")

print("✅ Fichiers générés avec succès.")


subprocess.run([
    "docker-compose",
    "-f", prometheus_compose_file,
    "down"
], check=False)

# Redémarrer les conteneurs en arrière-plan
subprocess.run([
    "docker-compose",
    "-f", prometheus_compose_file,
    "up", "-d"
], check=False)

subprocess.run([
    "docker-compose",
    "-f", ansible_compose_file,
    "down"
], check=False)

# Redémarrer les conteneurs en arrière-plan
subprocess.run([
    "docker-compose",
    "-f", ansible_compose_file,
    "up", "-d"
], check=False)


