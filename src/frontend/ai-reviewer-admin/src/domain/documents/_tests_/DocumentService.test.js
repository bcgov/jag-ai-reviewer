import api from "AxiosConfig";
import MockAdapter from "axios-mock-adapter";
import { getDocumentTypeConfigurations, submitNewDocumentTypeConfigurations, deleteDocumentTypeConfiguration } from "domain/documents/DocumentService";

import { configurations } from "domain/documents/_tests_/test-data";

describe("Document Service test suite", () => {
  let mockApi;
  beforeEach(() => {
    jest.clearAllMocks()
    mockApi = new MockAdapter(api);
  });

  test("getDocumentTypeConfigurations 200", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(200, configurations);

    const response = await getDocumentTypeConfigurations();
    expect(response).toMatchObject(configurations);
  });
  
  test("getDocumentTypeConfigurations 200 params", async () => {
    const subset = configurations.splice(0,1);
    mockApi.onGet("/documentTypeConfigurations", { params: { "documentType": "SG1" }}).reply(200, subset);

    const response = await getDocumentTypeConfigurations("SG1");
    expect(response).toMatchObject(subset);
  });

  test("getDocumentTypeConfigurations 401", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(401, configurations);

    try {
      await getDocumentTypeConfigurations();
    } catch (error) {
      expect(error.message).toEqual("Request failed with status code 401");
      expect(error.response.status).toEqual(401);
    }
  });
  
  test("getDocumentTypeConfigurations invalid", async () => {
    // api returns successfully, but with unexpected data.
    mockApi.onGet("/documentTypeConfigurations").reply(200, "unexpected data");

    try {
      await getDocumentTypeConfigurations();
    } catch (error) {
      expect(error.message).toEqual("Unexpected response from Diligen API");
    }
  });

  test("deleteDocumentTypeConfiguration 200", async () => {
    mockApi.onDelete("/documentTypeConfigurations/6550866d-754c-9d41-52a5-c229bc849ee3").reply(200);

    const response = await deleteDocumentTypeConfiguration("6550866d-754c-9d41-52a5-c229bc849ee3");
    expect(response.status).toEqual(200);
  });

  test("deleteDocumentTypeConfiguration 404", async () => {
    mockApi.onDelete("/documentTypeConfigurations/6550866d-754c-9d41-52a5-c229bc849ee3").reply(404);

    const response = deleteDocumentTypeConfiguration("6550866d-754c-9d41-52a5-c229bc849ee3");
    expect(response).rejects.toThrowError();
  });

  test("submitDocumentTypeConfigurations 200", async () => {
    const config = [{...configurations[0], documentType: "TEST3", documentTypeDescription: "Description"}];
    mockApi.onPost("/documentTypeConfigurations", config).reply(200, config);
    mockApi.onGet("/documentTypeConfigurations", {params: {"documentType": "TEST3"}}).reply(200, config);
    
    await submitNewDocumentTypeConfigurations(JSON.stringify(config));

    const response = await getDocumentTypeConfigurations("TEST3");
    expect(response).toMatchObject(config);
  })

  test("submitDocumentTypeConfigurations 400", async () => {
    const config = {...configurations[0], documentType: "TEST3", documentTypeDescription: "Description"}
    mockApi.onPost("/documentTypeConfigurations").reply(400);

    try {
      await submitNewDocumentTypeConfigurations(JSON.stringify(config));
    } catch (e) {
      expect(e.message).toEqual("Request failed with status code 400");
    }
  })
});
