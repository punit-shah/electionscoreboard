const fs = require("fs");
const Client = require("node-rest-client").Client;

class XmlFilePoster {
  constructor() {
    this.client = new Client();
    this.baseUrl = "http://localhost:8080";
    this.endpoint = "/constituency-results";
  }

  post(filename) {
    const args = {
      data: fs
        .readFileSync(`../scoreboard/src/main/resources/xml-files/${filename}`)
        .toString(),
      headers: { "Content-Type": "text/xml" }
    };

    this.client.post(
      `${this.baseUrl}${this.endpoint}`,
      args,
      (data, response) => {
        console.log(response.statusCode);
        data.errors && data.errors.forEach(e =>
          e.defaultMessage ? console.log(e.defaultMessage) : undefined
        );
      }
    );
  }
}

module.exports = XmlFilePoster;
