import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [adding, setAdding] = useState(false);

  // Fetch tasks on mount
  useEffect(() => {
    fetch("/api/tasks")
      .then((res) => {
        if (!res.ok) throw new Error(`Error: ${res.status}`);
        return res.json();
      })
      .then((tasks) => setTasks(tasks))
      .catch((err) => setError(err.message || "API error"))
      .finally(() => setLoading(false));
  }, []);

  const addTask = async (e) => {
    e.preventDefault();
    setAdding(true);
    setError("");
    try {
      const res = await fetch("/api/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, description: description || undefined }),
      });
      if (!res.ok) {
        const data = await res.json();
        throw new Error(data.error || "Failed to add task");
      }
      const task = await res.json();
      setTasks((tasks) => [...tasks, task]);
      setTitle("");
      setDescription("");
    } catch (err) {
      setError(err.message || "Failed to add task");
    } finally {
      setAdding(false);
    }
  };

  return (
    <div className="App">
      <h1>Todo Tasks</h1>
      <form onSubmit={addTask} style={{ marginBottom: "1em" }}>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Task title"
          required
          style={{ marginRight: "0.5em" }}
        />
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Description (optional)"
          style={{ marginRight: "0.5em" }}
        />
        <button type="submit" disabled={adding || !title.trim()}>
          {adding ? "Adding..." : "Add Task"}
        </button>
      </form>
      {error && <div style={{ color: "red" }}>{error}</div>}
      {loading ? (
        <div>Loading tasks...</div>
      ) : (
        <>
          {tasks.length === 0 ? (
            <div>No tasks found.</div>
          ) : (
            <ul>
              {tasks.map((t) => (
                <li key={t.id}>
                  <b>{t.title}</b>
                  {t.description && <> &mdash; {t.description}</>}
                  <span style={{ color: "green", marginLeft: 8 }}>
                    [{t.done ? "done" : "pending"}]
                  </span>
                  <span style={{ color: "#888", marginLeft: 12, fontSize: "80%" }}>
                    {new Date(t.createdAt).toLocaleString()}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </>
      )}
    </div>
  );
}

export default App;
