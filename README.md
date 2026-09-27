# DigestCalculator

## Autores

- João Pedro Zaidman dos Santos Gonçalves — 2320464
- Breno de Andrade Soares — 2320363

## Proposta

Programa Java que calcula e verifica os digests dos arquivos de uma pasta usando
um catálogo XML.

Algoritmos disponíveis: `MD5`, `SHA1`, `SHA256` e `SHA512`.

## Como executar

É necessário ter o JDK instalado. Todos os comandos devem ser executados na
pasta raiz do projeto.

### Compilar

Com o Makefile:

```powershell
make compile
```

Ou diretamente no terminal:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -d bin src\*.java
```

### Executar

Com o Makefile, usando `SHA256`, `digestsList.xml` e `monitoredFiles`:

```powershell
make run
```

Ou diretamente no terminal:

```powershell
java -cp bin DigestCalculator SHA256 digestsList.xml monitoredFiles
```

Para escolher outros valores:

```powershell
make run ALGORITHM=SHA512 DIGEST_LIST="digestsList.xml" MONITORED_DIR="monitoredFiles"
```

```powershell
java -cp bin DigestCalculator SHA512 digestsList.xml monitoredFiles
```

### Limpar os arquivos compilados

Com o Makefile:

```powershell
make clean
```

Ou diretamente no terminal:

```powershell
Get-ChildItem -Path . -Filter '*.class' -Recurse | Remove-Item -Force
```

Ao encontrar um arquivo sem um digest registrado para o algoritmo escolhido, o
programa adiciona o novo digest ao catálogo XML.
