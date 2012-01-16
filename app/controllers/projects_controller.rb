class ProjectsController < ApplicationController
  before_filter :authenticate_user!
  respond_to :html, :json

  # TECHNICAL_DEBT: this isn't looked up directly in the controller action, and
  # so load_and_authorize_resource wont work.

  def show
    @project = Project.from_github_name params[:project]
    authorize! :read, @project

    respond_with @project do |f|
      f.html
      # Security - Devise restricts this to only return attr_accessible attributes.
      f.json { render :json => @project }
    end
  end

  def edit
    @project = Project.from_github_name params[:project]
    authorize! :read, @project
  end

  def

  def update
    @project = Project.from_github_name params[:project]
    authorize! :manage, @project

    @project.update_spec(params["spec"])
    @project.save!

    # Automatically trigger another build, since after saving, you'll always want another build to run to test it.
#    Backend.build(@project)

  end
end
