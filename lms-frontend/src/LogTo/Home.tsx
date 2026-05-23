import SignIn from "./SignIn";

const Home = () => {
    return (
            <div className="App">
                <header className="App-header">
                    <SignIn />
                </header>
            </div>
          );
}

// import { useLogto } from '@logto/react';

// const Home = () => {
//   const { signIn, signOut, isAuthenticated } = useLogto();

//   return isAuthenticated ? (
//     <button onClick={() => signOut('http://localhost:3000/callback')}>Sign Out</button>
//   ) : (
//     <button onClick={() => signIn('http://localhost:3000/callback')}>Sign In</button>
//   );
// };

export default Home;