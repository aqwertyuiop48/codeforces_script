require("@nomicfoundation/hardhat-toolbox");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.27",
  networks: {
    // This configures the node when you run 'npx hardhat node'
    hardhat: {
      blockGasLimit: 12000000,
      gas: 12000000
    },
    // This configures the connection when you use '--network localhost'
    localhost: {
      url: "http://127.0.0.1:8545",
      blockGasLimit: 12000000,
      gas: 12000000
    }
  }
};