import React from "react";
import { render, screen } from "@testing-library/react";
import App from "./App";

jest.mock("domain/authentication/AuthenticationGuard", () => ({
  __esModule: true,
  default: () => null,
}));

describe("App test suite", () => {
  test("renders App", () => {
    render(<App />);
    const titleInHeader = screen.getByText(/AI Reviewer Admin Client/i);
    expect(titleInHeader).toBeInTheDocument();
  });
});
