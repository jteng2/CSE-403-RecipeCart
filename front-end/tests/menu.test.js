import React from "react";
import { render, fireEvent, screen } from "@testing-library/react"; 
import ProfileButton from "../app/components/ProfileButton";

test("toggle menu", () => {
    render(<ProfileButton />);

    const profile = screen.getByTestId("profile-button");
    expect(() => {
        screen.getByTestId("profile-menu");
    }).toThrow();
    fireEvent.click(profile);

    const menu = screen.getByTestId("profile-menu");


    expect(menu).toBeDefined();

});
