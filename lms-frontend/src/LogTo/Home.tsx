import { useLogto } from "@logto/react";
import SignIn from "./SignIn";
import SignOut from "./SignOut";

const Home = () => {
    const { isAuthenticated } = useLogto();

    return (
            <div className="App">
                <header className="App-header">
                    <SignIn />
                    {isAuthenticated && <SignOut />}
                </header>
            </div>
          );
}

export default Home;