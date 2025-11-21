# Todo Frontend (React + Webpack)

Minimal React app to view and add tasks using the Spring Boot backend.

## Getting Started

### Prerequisites

- Node.js (18+)
- npm (comes with Node)

### Install and run

```sh
cd frontend
npm install          # Install dependencies
npm run dev          # Start webpack dev server (http://localhost:5173)
```

### Production build

```sh
npm run build        # Output will be in frontend/dist
```

### API Proxy

Webpack dev server proxies `/api` requests to the backend on `http://localhost:8080`.

### Add a Task

- Enter a title (required) and description (optional)
- Click "Add Task"
- List auto-refreshes with the new task

### Backend required

You must have the Spring Boot backend running at [http://localhost:8080](http://localhost:8080).

Start it with:
```sh
cd ..
mvn spring-boot:run
```

### Full-Stack Workflow

Open **two terminals**:
1. Terminal 1:
   ```sh
   cd todo-backend
   mvn spring-boot:run
   ```
2. Terminal 2:
   ```sh
   cd todo-backend/frontend
   npm install
   npm run dev
   ```

Then visit [http://localhost:5173](http://localhost:5173)  
Enjoy your full stack Java + React Todo app!

---
MIT/Apache/OSS stack.
