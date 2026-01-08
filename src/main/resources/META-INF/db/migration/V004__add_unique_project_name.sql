CREATE UNIQUE INDEX IF NOT EXISTS uk_projects_name_lower
  ON projects (LOWER(name));
