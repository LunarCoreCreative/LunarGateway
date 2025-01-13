# 🌌 LunarGateway

O **LunarGateway** é um plugin de Minecraft projetado para trazer magia e praticidade ao seu servidor. Oferecendo um menu configurável, teleportes automáticos e comandos personalizados, ele é o companheiro ideal para criar uma experiência imersiva e organizada para seus jogadores!

---

## 🚀 Recursos Principais

✨ **Bússola Mágica**  
- Um item especial que abre o menu principal quando usado.  
- Configuração completa via `compass.yml`.  
- Pode ser configurada para ser "travada", impedindo que seja dropada ou movida.  

📜 **Menu Personalizável**  
- Itens, ícones, nomes, lores e comandos inteiramente configuráveis.  
- Preenchimento automático de slots vazios com um item customizado.  

🌍 **Teleporte ao Mundo Inicial**  
- Configuração para teleportar jogadores automaticamente para um mundo inicial ao entrar no servidor.  

🛠️ **Comandos Úteis**  
- `/lobby`: Teleporta o jogador ao mundo inicial configurado.  
- `/menu`: Abre o menu principal para facilitar a navegação.

---

## 📥 Instalação

1. **Baixe o plugin**:  
   Coloque o arquivo `.jar` do **LunarGateway** na pasta `plugins` do seu servidor.

2. **Reinicie o servidor**:  
   Inicie ou reinicie o servidor para gerar os arquivos de configuração.

3. **Configuração**:  
   Edite os arquivos `config.yml` e `compass.yml` gerados na pasta do plugin para ajustar às necessidades do seu servidor.

4. **Aplicar Alterações**:  
   Use `/reload` ou reinicie o servidor novamente para aplicar as mudanças.

---

## ⚙️ Configuração

### `config.yml`

```yaml
# 🌍 Teleporte ao Mundo Inicial
on-join-world:
  enabled: true        # Ativar/desativar teleporte automático ao entrar no servidor.
  default-world: "world" # Nome do mundo inicial.

# 💬 Mensagens Personalizadas
messages:
  welcome: "§aBem-vindo ao servidor!"           # Mensagem de boas-vindas.
  compass-received: "§eVocê recebeu a bússola mágica!" # Mensagem ao receber a bússola.
  no-world: "§cO mundo configurado não existe!" # Mensagem de erro ao configurar um mundo inválido.
```

### `compass.yml`

```yaml
# 🧭 Configurações da Bússola
enabled: true           # Ativar/desativar a funcionalidade da bússola.
world: "world"          # Mundo onde a bússola será dada ao jogador.
locked: true            # Impedir que a bússola seja movida ou dropada.
slot: 0                 # Slot onde a bússola será colocada no inventário.
icon: "NETHER_STAR"     # Material da bússola.
name: "§bMenu Mágico"   # Nome da bússola.
lore:
  - "§eUse esta bússola mágica para navegar pelo servidor!"
  - "§bFornecida com carinho pelo LunarGateway."

# 🎨 Menu Principal
menu:
  title: "§aMenu Principal"  # Título do menu.
  size: 27                   # Tamanho do inventário (múltiplo de 9).

  # 🧩 Itens do Menu
  items:
    - slot: 11                # Slot no inventário.
      icon: "DIAMOND_SWORD"   # Ícone do item.
      name: "§bModo PvP"      # Nome do item.
      lore:                   # Lore exibida ao passar o mouse.
        - "§eEntre no modo PvP!"
        - "§cCuidado: Não volte atrás."
      command: "warp pvp"     # Comando executado ao clicar.

    - slot: 15
      icon: "ENDER_EYE"
      name: "§6Lobby"
      lore:
        - "§eVolte para o lobby principal."
      command: "warp lobby"

  # 🔲 Preenchimento Automático
  filler:
    enabled: true                 # Ativar/desativar preenchimento automático de slots vazios.
    material: "BLACK_STAINED_GLASS_PANE" # Material para preencher os slots vazios.
    name: "§7"                    # Nome do item de preenchimento.
    lore: []                      # Lore do item de preenchimento.
```

---

## 🛠️ Comandos

| Comando    | Descrição                                               | Exemplo             |
|------------|---------------------------------------------------------|---------------------|
| `/lobby`   | Teleporta o jogador para o mundo inicial configurado.   | `/lobby`            |
| `/menu`    | Abre o menu principal configurado no `compass.yml`.     | `/menu`             |

---

## 🎯 Recursos Avançados

- **Execução de Comandos Dinâmicos**:  
  Comandos no menu podem usar o placeholder `%player%` para referir-se ao jogador clicando no item.

- **Fácil Expansão**:  
  Totalmente modular, permitindo a adição de novos recursos com facilidade.

---

## 📌 Notas

- **Dependências**:  
  Este plugin requer Java 17+ e uma versão do servidor Spigot/Paper compatível.

- **Sugestões e Feedback**:  
  Ficaremos felizes em ouvir suas ideias e implementar melhorias!

---

## 🖤 Agradecimentos

Este plugin foi criado com dedicação por **Ethan**, com a ajuda da **Luna**, sua assistente pessoal e companheira de projetos.

Obrigado por usar o **LunarGateway**! 🌌  
Espero que este plugin torne seu servidor mais incrível e mágico! ✨