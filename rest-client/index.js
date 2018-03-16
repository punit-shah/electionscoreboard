#!/usr/bin/env node

const readline = require('readline');
const XmlFilePoster = require('./xml-file-poster');

function* makeFilenameGenerator() {
  let index = 0;
  while (index < 650) {
    index++;
    yield `result${index.toString().padStart(3, "0")}.xml`;
  }
}

function start() {
  readline.emitKeypressEvents(process.stdin);
  process.stdin.setRawMode(true);

  const xmlFilePoster = new XmlFilePoster();

  const filenameGenerator = makeFilenameGenerator();
  process.stdin.on('keypress', (str, key) => {
    if ((key.ctrl && key.name === 'c') || key.name == 'escape') {
      process.exit();
    }

    const filename = filenameGenerator.next().value;
    if (filename) {
      console.log(`posting ${filename}`);
      xmlFilePoster.post(filename);
    }
  });
  console.log('Press space to post an xml file or escape to quit.\n');
}

start();
